/****
 * This is the Main View for the Panic Button. 
 * It listens for the Panic Button Press and triggers the Panic Notification to the Service.
 * */

package com.sigtech.panicbutton.frags;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.sigtech.panicbutton.R;
import com.sigtech.panicbutton.acts.MainSFAct;
import com.sigtech.panicbutton.manager.DataManager;
import com.sigtech.panicbutton.manager.DatabaseManager;

public class MainPanicFragment extends SherlockFragment implements
		OnClickListener {

	Button b;
	ImageView iv;
	public static TextView tv_long, tv_lat, mode_status;
	static Activity act;
	ProgressDialog pd;
	private static boolean ACTION_NOT_TAKEN = false;
	AlertDialog ad;
	DatabaseManager dbmgr;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act = this.getActivity();
		((MainSFAct) act).invalidateOptionsMenu();
		DataManager.ON_MAIN_PANIC_SCREEN = true;

//		 new DatabaseManager(getActivity()).dropTableAndReInit();

		View v = inflater.inflate(R.layout.main_panic, null);

		pd = ProgressDialog.show(this.getActivity(), "Location",
				"Getting location...");

		setupViews(v);

		pd.dismiss();

		dbmgr = new DatabaseManager(act);
		DataManager.showLongToast(act, dbmgr.getInfoAccessToken());
		if (dbmgr.infoPINIsEmpty())
			((MainSFAct) act).enterPin(0);

		return v;
	}

	private void setupViews(View v) {
		b = (Button) v.findViewById(R.id.panic_button);
		tv_lat = (TextView) v.findViewById(R.id.tv_lat);
		tv_long = (TextView) v.findViewById(R.id.tv_long);
		mode_status = (TextView) v.findViewById(R.id.mode_status);

		if (DataManager.latitude != -1 && DataManager.longitude != -1) {
			tv_lat.setText("Lat: " + DataManager.latitude);
			tv_long.setText("Long: " + DataManager.longitude);
			DataManager.LATLNG_SET = true;
		}

		b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		View v1 = getLayoutInflater(null).inflate(R.layout.doublepanic, null);
		ad = new AlertDialog.Builder(act).setView(v1).create();
		v1.findViewById(R.id.doublepanic).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!DataManager.LATLNG_SET) {
							Toast.makeText(act,
									"Waiting for location... Panicking...",
									Toast.LENGTH_SHORT).show();
							notifyClicked();
							return;
						}
						((MainSFAct) act).mBoundService
								.notifyPanicButtonPressed();
						ad.dismiss();
					}
				});
		ad.show();
	}

	private void notifyClicked() {
		ACTION_NOT_TAKEN = true;
	}

	public static void notifyLocationUpdated() {
		if (ACTION_NOT_TAKEN) {
			((MainSFAct) act).mBoundService.notifyPanicButtonPressed();
			ACTION_NOT_TAKEN = false;
		}
	}

	@Override
	public void onDestroyView() {
		DataManager.ON_MAIN_PANIC_SCREEN = false;
		super.onDestroyView();
	}

}
