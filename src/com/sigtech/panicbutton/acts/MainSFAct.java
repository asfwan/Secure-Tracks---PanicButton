/****
 * This is the Main Activity that starts the service and binds with it. 
 * This is the core controller for the pages of the app.
 * */

package com.sigtech.panicbutton.acts;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.sigtech.panicbutton.R;
import com.sigtech.panicbutton.frags.AboutFragment;
import com.sigtech.panicbutton.frags.FollowersFragment;
import com.sigtech.panicbutton.frags.FormFragment;
import com.sigtech.panicbutton.frags.LoginFragment;
import com.sigtech.panicbutton.frags.MainPanicFragment;
import com.sigtech.panicbutton.frags.UnlockerFragment;
import com.sigtech.panicbutton.manager.DataManager;
import com.sigtech.panicbutton.manager.DatabaseManager;
import com.sigtech.panicbutton.manager.internet.LogoutManager;
import com.sigtech.panicbutton.service.MainService;

public class MainSFAct extends SherlockFragmentActivity {
	private void DEBUGGING_TEST() {
		// new ActivationManager(this).execute();
		// new LocationUpdater(this).disableTrigger();
		// new LocationUpdater(this).updateLocationOnEmergency();
		// new ContactRetriever(this).execute();

		// dbmgr.dropTableAndReInitBoth();
		// dbmgr.dropTableAndReInitWOClosing();

		// dbmgr.testSavedDetails();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (!DataManager.LOGGED_IN) {
			return false;
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		super.onResume();
		DataManager.MAIN_IS_OPEN = true;
	}

	public MainService mBoundService = null;

	public boolean mIsBound = false;

	private static MainSFAct sInstance = null;

	DatabaseManager dbmgr = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startService(new Intent(this, MainService.class));

		dbmgr = new DatabaseManager(this);
		dbmgr.disableAutoClose();

		sInstance = this;
		setContentView(R.layout.main_blank);

		if (dbmgr.getId() == null)
			loginScreen();
		else {
			DataManager.LOGGED_IN = true;
			mainScreen();
		}
		if (dbmgr.checkFirstTime()) {
			splashScreen();
		}
		doBindService();

		DEBUGGING_TEST();
	}

	public static MainSFAct getInstance() {
		return sInstance;
	}

	void onServiceConnected() {
		// mBoundService.showToast("Service is Connected!");
	}

	private void replaceFragment(Fragment f) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, f).commit();
	}

	@Override
	protected void onDestroy() {
		doUnbindService();
		DataManager.MAIN_IS_OPEN = false;
		if (!dbmgr.isClosed())
			dbmgr.closeAndNullify();
		super.onDestroy();
	}

	void doBindService() {
		bindService(new Intent(MainSFAct.this, MainService.class), mConnection,
				Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	void doUnbindService() {
		if (mIsBound) {
			unbindService(mConnection);
			mIsBound = false;
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBoundService = ((MainService.LocalBinder) service).getService();
			// mBoundService.showToast("Connected!");
			MainSFAct.this.onServiceConnected();
		}

		public void onServiceDisconnected(ComponentName className) {
			mBoundService = null;
			Toast.makeText(MainSFAct.this, "Disconnected!", Toast.LENGTH_SHORT)
					.show();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (DataManager.PREVENT_BACK_BUTTON)
				return false;
			// else if (!DataManager.ON_MAIN_PANIC_SCREEN) {
			// mainScreen();
			// return false;
			// }
		}
		// Toast.makeText(this, ""+keyCode, Toast.LENGTH_SHORT).show();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.changepin:
			enterPin(1);
			break;
		case R.id.about:
			aboutScreen();
			break;
		// case R.id.addfollowers:
		// followersScreen();
		// break;
		case R.id.logout:
			DataManager.LOGGED_IN = false;
			new LogoutManager(sInstance).logout(dbmgr.getId());
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	public void lockScreen() {
		replaceFragment(new UnlockerFragment());
	}

	public void unlockScreen() {
		replaceFragment(new MainPanicFragment());
	}

	public void mainScreen() {
		replaceFragment(new MainPanicFragment());
	}

	public void aboutScreen() {
		replaceFragment(new AboutFragment());
	}

	public void followersScreen() {
		replaceFragment(new FollowersFragment());
	}

	public void formScreen() {
		replaceFragment(new FormFragment());
	}

	public void loginScreen() {
		replaceFragment(new LoginFragment());
	}

	public void splashScreen() {
		startActivity(new Intent(this, SplashAct.class));
	}

	public void panicMessageScreen() {
		startActivity(new Intent(this, PanicMessageAct.class));
	}

	public void showToast(String txt) {
		Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
	}

	public void startDummyPage(String data) {
		Intent intent = new Intent(this, DummyPageTest.class);
		intent.putExtra("data", data);
		startActivity(intent);
	}

	public void enterPin(int flag) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		final EditText et = new EditText(this);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		et.setHint("Enter old PIN");

		final EditText et_new_pin = new EditText(this);
		et_new_pin.setInputType(InputType.TYPE_CLASS_NUMBER);
		et_new_pin.setHint("Enter new PIN");
		LinearLayout par = new LinearLayout(this);
		par.setOrientation(LinearLayout.VERTICAL);

		par.addView(et);
		OnClickListener newadlistener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String newPin = et.getText().toString();
				if (!newPin.equals(""))
					dbmgr.saveInfoPIN(newPin);
			}
		};
		OnClickListener oldadlistener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String oldPin = et.getText().toString();
				String newPin = et_new_pin.getText().toString();
				if (!oldPin.equals("") && !newPin.equals("")) {
					if (dbmgr.getInfoPIN().equals(oldPin)) {
						dbmgr.saveInfoPIN(newPin);
						showToast("PIN was changed!!");
					}
				}
			}
		};
		builder.setTitle("Set your PIN!")
				.setView(par)
				.setCancelable(false)
				.setPositiveButton("SET",
						flag == 0 ? newadlistener : oldadlistener);
		if (flag > 0) {
			builder.setCancelable(true);

			par.addView(et_new_pin);
			builder.setNegativeButton("Cancel", null);
		} else
			et.setHint("Enter your PIN!");
		AlertDialog ad = builder.create();
		ad.show();
	}
}
