package com.sigtech.panicbutton.manager.internet;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.sigtech.panicbutton.acts.MainSFAct;
import com.sigtech.panicbutton.manager.DataManager;
import com.sigtech.panicbutton.manager.DatabaseManager;

public class LogoutManager extends AsyncTask<String, Void, String> {
	private static final String TAG = "LOGOUT MANAGER";
	Activity act;
	private String url;

	public LogoutManager(Activity act) {
		this.act = act;
	}

	@Override
	protected String doInBackground(String... params) {
		String url = null;
		if (params.length > 0)
			url = params[0];

		if (url != null) {
			return DataManager.deleteData(url);
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null) {
			new DatabaseManager(act).saveUserApproval("0");
			// new DatabaseManager(act).testSavedDetails();
			String resp = null;
			try {
				resp = new JSONObject(result).get("status").toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (resp.equals("true")) {
				((MainSFAct) act).loginScreen();
				new DatabaseManager(act).dropTableAndReInit();
			}
		}
		if (DataManager.DEBUGGING)
			Log.d(TAG, "LogoutManager returns" + result);
	}

	public void logout(String id) {
		this.url = DataManager.URL_LOGOUT_1 + id + DataManager.URL_LOGOUT_2;
		execute(url);
	}
}
