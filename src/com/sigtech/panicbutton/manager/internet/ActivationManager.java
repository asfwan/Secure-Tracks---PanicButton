package com.sigtech.panicbutton.manager.internet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.sigtech.panicbutton.manager.DataManager;
import com.sigtech.panicbutton.manager.DatabaseManager;

import android.content.Context;
import android.util.Log;

public class ActivationManager extends InternetManager {
	private static final String TAG = "ACTIVATION MANAGER";
	DatabaseManager dbmgr;

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (DataManager.DEBUGGING)
			DataManager.showLongToast(context, result);
		if (DataManager.DEBUGGING)
			Log.d(TAG, "ActivationManager returns" + result);
	}

	@Override
	public List<NameValuePair> setupData() {

		String access_token = dbmgr.getInfoAccessToken();
		String latitude = String.valueOf(DataManager.latitude);
		String longitude = String.valueOf(DataManager.longitude);
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("access_token", access_token));
		nvp.add(new BasicNameValuePair("latitude", latitude));
		nvp.add(new BasicNameValuePair("longitude", longitude));
		return nvp;
	}

	public ActivationManager(Context context) {
		super(context);
		dbmgr = new DatabaseManager(context);

		setUrl(DataManager.URL_ACTIVATION_1 + dbmgr.getId()
				+ DataManager.URL_ACTIVATION_2);
		if (DataManager.DEBUGGING)
			Log.d(TAG, "ActivationManager url: " + url);
	}

}
