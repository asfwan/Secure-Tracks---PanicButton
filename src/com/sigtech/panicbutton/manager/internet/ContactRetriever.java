package com.sigtech.panicbutton.manager.internet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sigtech.panicbutton.manager.DataManager;
import com.sigtech.panicbutton.manager.DatabaseManager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ContactRetriever extends AsyncTask<String, Void, String> {
	private static final String TAG = "CONTECT RETRIEVER";
	// every day
	DatabaseManager dbmgr;
	Context context;

	public ContactRetriever(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		dbmgr = new DatabaseManager(context);

		return DataManager.getData(DataManager.URL_RETRIEVE_CONTACT_1
				+ dbmgr.getId() + DataManager.URL_RETRIEVE_CONTACT_2);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			JSONObject jObj = new JSONObject(result);
			JSONArray tracking_gr = jObj.getJSONArray("tracking_granted_to");
			int len = tracking_gr.length();
			String phone_nums[] = new String[len];

			for (int i = 0; i < len; i++) {
				phone_nums[i] = tracking_gr.getJSONObject(i).getString("phone");

				if (i == 0)
					dbmgr.savePanicNum(phone_nums[i]);
				else
					dbmgr.addPanicNum(phone_nums[i]);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// DataManager.showLongToast(context, dbmgr.getPanicNum());
		if (DataManager.DEBUGGING)
			Log.d(TAG, "ContactRetriever returns " + result);
	}
}
