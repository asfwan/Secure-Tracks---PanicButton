/****
 * This is the Main Service that runs in the background 
 * */

package com.sigtech.panicbutton.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.sigtech.panicbutton.acts.MainSFAct;
import com.sigtech.panicbutton.frags.MainPanicFragment;
import com.sigtech.panicbutton.manager.DataManager;
import com.sigtech.panicbutton.manager.MyLocationListener;
import com.sigtech.panicbutton.manager.PanicManager;
import com.sigtech.panicbutton.manager.internet.ActivationManager;
import com.sigtech.panicbutton.manager.internet.ServerStatusManager;

public class MainService extends Service {
	MainSFAct mMain = null;
	LocationManager manager;
	Location loc;
	MyLocationListener listener = null;
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// private static final long SERVER_CHECK_INTERVAL = 60 * 1000;
	public static MainService cService = null;
	static int SYNC_NUM = 0;
	AlarmManager aM;

	@Override
	public void onCreate() {
		super.onCreate();
		// showToast("MainService-onCreate()");// this is CONFIRMED RUNNING
		cService = this;

		aM = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		setupLocationListener();

	}

	// private void scheduleServerStatusChecking() {
	// // setup server status command
	//
	// // aM.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
	// // SystemClock.elapsedRealtime(), SERVER_CHECK_INTERVAL,
	// // PendingIntent
	// // .getService(this, 0, new Intent(this,
	// // ServerChecker.class),
	// // PendingIntent.FLAG_CANCEL_CURRENT));
	// aM.cancel(PendingIntent.getService(this, 0, new Intent(this,
	// ServerChecker.class), PendingIntent.FLAG_CANCEL_CURRENT));
	// }

	private void checkServerStatus() {
		new ServerStatusManager(cService).execute();
	}

	public void callActivity(Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		// showToast("MainService-onRebind()");// this is CONFIRMED NOT RUNNING
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// showToast("MainService-onStartCommand()");// this is CONFIRMED NOT
		// RUNNING
		return START_STICKY;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// showToast("MainService-onUnbind()");// this is CONFIRMED RUNNING
		mMain = null;
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		// showToast("MainService-onDestroy()");// this is CONFIRMED RUNNING
		if (listener != null)
			manager.removeUpdates(listener);
		super.onDestroy();
	}

	LocalBinder binder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// showToast("MainService-onBind()");// this is CONFIRMED RUNNING
		renewUIInstance();
		return binder;
	}

	public class LocalBinder extends Binder {
		public MainService getService() {
			return MainService.this;
		}
	}

	public void showToast(String txt) {
		Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
	}

	public void notifyUnlocked() {
		renewUIInstance();
		mMain.unlockScreen();
		settleDownPanic();
	}

	public void notifyLocked() {
		renewUIInstance();
		mMain.lockScreen();
		// new LocationUpdater(cService).updateLocationOnEmergency(true);
	}

	public void settleDownPanic() {
		DataManager.PANICKED = false;
		PanicManager.onPanicStopped(mMain, MainPanicFragment.tv_lat.getText()
				.toString(), MainPanicFragment.tv_long.getText().toString());
		checkAndRenewUIInstance();
		mMain.mainScreen();
		// new LocationUpdater(cService).updateLocationOnEmergency(false);
	}

	public void openMain() {
		callActivity(new Intent(this, MainSFAct.class));
	}

	public void renewUIInstance() {
		mMain = MainSFAct.getInstance();
	}

	public void checkAndRenewUIInstance() {
		if (mMain == null)
			mMain = MainSFAct.getInstance();
	}

	@SuppressLint("HandlerLeak")
	public void notifyPanicButtonPressed() {
		checkAndRenewUIInstance();
		if (!DataManager.PANICKED) {
			DataManager.PANICKED = true;
			PanicManager.initiatePanic(mMain, MainPanicFragment.tv_lat
					.getText().toString(), MainPanicFragment.tv_long.getText()
					.toString());
			MainPanicFragment.mode_status.setText("PANICKING...");
			notifyLocked();
		} else {
			Toast.makeText(this, "Panic Mode initiated...", Toast.LENGTH_SHORT)
					.show();
		}
		checkServerStatus();
	}

	private void setupLocationListener() {
		listener = new MyLocationListener(this);
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		String bestProvider = manager.getBestProvider(new Criteria(), false);
		String gpsProvider = LocationManager.GPS_PROVIDER;
		String networkProvider = LocationManager.NETWORK_PROVIDER;
		String prv[] = new String[] { networkProvider, gpsProvider,
				bestProvider };

		for (int i = 0; i < 3; i++) {
			if (DataManager.LATLNG_SET)
				break;
			if (!manager.isProviderEnabled(prv[i]))
				continue;
			loc = manager.getLastKnownLocation(prv[i]);
			displayLocation();
		}

		bestProvider = manager.getBestProvider(new Criteria(), true);
		if (bestProvider != null) {
			if (bestProvider.equals(gpsProvider)) {
				DataManager.showToast(cService, "Best Provider=GPS");
				manager.requestLocationUpdates(gpsProvider,
						MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,
						listener);
				scheduleGPSTimeout_changeToNetwork();
				// setTimer for locking
			} else if (bestProvider.equals(networkProvider)) {
				DataManager.showToast(cService, "Best Provider=Network");
				manager.requestLocationUpdates(networkProvider,
						MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,
						listener);
			}
		} else {
			showToast("Warning! No enabled provider!" + bestProvider);
			showWarning();
		}

	}

	private void showWarning() {
		if(mMain!=null) mMain.finish();
	}

	@SuppressLint("HandlerLeak")
	private void scheduleGPSTimeout_changeToNetwork() {

		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				manager.removeUpdates(listener);
				manager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
				if (!DataManager.LATLNG_SET_EXACT)
					scheduleGPSTimeout_changeToGPS();
				Toast.makeText(cService, "Timeout occurs [GPS to network..]",
						Toast.LENGTH_SHORT).show();
			}
		};
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!DataManager.LATLNG_SET || !DataManager.LATLNG_SET_EXACT)
					h.sendEmptyMessage(0);
				else {
					DataManager.LATLNG_SET_EXACT = true;
					// if (cService != null)
					// new ActivationManager(cService).execute();
				}

			}
		}, 10000);
	}

	@SuppressLint("HandlerLeak")
	private void scheduleGPSTimeout_changeToGPS() {
		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				manager.removeUpdates(listener);
				manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,
						listener);
				if (!DataManager.LATLNG_SET_EXACT)
					scheduleGPSTimeout_changeToNetwork();
				Toast.makeText(cService, "Timeout occurs [Network to GPS..]",
						Toast.LENGTH_SHORT).show();
			}
		};
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!DataManager.LATLNG_SET || !DataManager.LATLNG_SET_EXACT)
					h.sendEmptyMessage(0);
				else {
					DataManager.LATLNG_SET_EXACT = true;
					// if (cService != null)
					// new ActivationManager(cService).execute();
				}
			}
		}, 10000);
	}

	// private void scheduleGPSTimeout_notYetSet() {
	// final Handler h = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// manager.removeUpdates(listener);
	// manager.requestLocationUpdates(
	// LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
	// MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
	// }
	// };
	// new Runnable() {
	// @Override
	// public void run() {
	// try {
	// Thread.sleep(15000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }.run();
	// }

	private void displayLocation() {
		if (DataManager.ON_MAIN_PANIC_SCREEN && loc != null) {
			MainPanicFragment.tv_lat.setText("Last Known LAT: "
					+ String.valueOf(loc.getLatitude()));
			MainPanicFragment.tv_long.setText("Last Known LONG: "
					+ String.valueOf(loc.getLongitude()));
			DataManager.LATLNG_SET = true;
		}
		if (DataManager.LOGGED_IN)
			new ActivationManager(cService).execute();
	}

	public static void notifyServerStatusChecked() {
		// initServerControl();
		// cService.showToast("SYNC " + (SYNC_NUM++));
	}

	// private static void initServerControl() {
	// if (DataManager.SERVER_STATUS.equals("LOCK")) {
	// if (!DataManager.MAIN_IS_OPEN)
	// cService.openMain();
	// cService.notifyLocked();
	// }
	// if (DataManager.SERVER_STATUS.equals("UNLOCK"))
	// if (!DataManager.MAIN_IS_OPEN)
	// cService.openMain();
	// cService.notifyUnlocked();
	//
	// if (DataManager.MAIN_IS_OPEN
	// && DataManager.SERVER_STATUS.equals("CLOSE")) {
	// if (cService.mMain == null)
	// cService.mMain = MainSFAct.getInstance();
	// cService.mMain.finish();
	// }
	// if (!DataManager.MAIN_IS_OPEN
	// && (DataManager.SERVER_STATUS.equals("MAIN") || DataManager.SERVER_STATUS
	// .equals("OPEN")))
	// cService.openMain();
	// }

}
