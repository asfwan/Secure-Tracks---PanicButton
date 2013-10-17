package com.sigtech.panicbutton.manager.internet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sigtech.panicbutton.manager.DataManager;
import com.sigtech.panicbutton.manager.DatabaseManager;

public class LocationUpdater extends AsyncTask<String, Void, String> {
	private static final String TAG = "LOCATION UPDATER";
	String url;
	DatabaseManager dbmgr;
	Context context;
	List<NameValuePair> nvp = null;

	public LocationUpdater(Context context) {
		this.context = context;
		dbmgr = new DatabaseManager(context);
	}

	@Override
	protected String doInBackground(String... params) {
		String response = null;

		response = DataManager.putData(url, nvp);

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		// DataManager.showLongToast(context, result);
		if (DataManager.DEBUGGING)
			Log.d(TAG, "LocationUpdater returns" + result);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void updateLocation() {
		setUrl(DataManager.URL_UPDATE_LOCATION_1 + dbmgr.getId()
				+ DataManager.URL_UPDATE_LOCATION_2);

		nvp = new ArrayList<NameValuePair>();

		nvp.add(new BasicNameValuePair("access_token", dbmgr
				.getInfoAccessToken()));
		nvp.add(new BasicNameValuePair("latitude", "" + DataManager.latitude));
		nvp.add(new BasicNameValuePair("longitude", "" + DataManager.longitude));
		// internet trigger disabled
		 execute();
		// ((MainSFAct) context).startDummyPage("" + jObj);
		DataManager.showLongToast(context, "LOCATION UPDATE");
	}

	public void updateLocationOnEmergency(boolean triggering) {
		DataManager.TRIGGERING = triggering;
		setUrl(DataManager.URL_UPDATE_LOCATION_1 + dbmgr.getId()
				+ DataManager.URL_UPDATE_LOCATION_EMERGENCY_3);

		nvp = new ArrayList<NameValuePair>();

		nvp.add(new BasicNameValuePair("access_token", dbmgr
				.getInfoAccessToken()));
		nvp.add(new BasicNameValuePair("latitude", "" + DataManager.latitude));
		nvp.add(new BasicNameValuePair("longitude", "" + DataManager.longitude));
		nvp.add(new BasicNameValuePair("triggering", ""
				+ DataManager.TRIGGERING));
//		DataManager.showLongToast(context, "EMERGENCY LOCATION UPDATE");
		// internet trigger disabled
		// execute();
	}

	public void enableTrigger() {
		DataManager.TRIGGERING = true;
	}

	public void disableTrigger() {
		DataManager.TRIGGERING = false;
	}

}
