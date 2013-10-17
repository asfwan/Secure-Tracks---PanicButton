/****
 * This is the main manager for the server-client communication.
 * */

package com.sigtech.panicbutton.manager.internet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.sigtech.panicbutton.manager.ConnectionChecker;
import com.sigtech.panicbutton.manager.DataManager;

import android.content.Context;
import android.os.AsyncTask;

public class InternetManager extends AsyncTask<String, Void, String> {
	String url = null;
	protected String[] params = null;
	Context context = null;

	@Override
	protected String doInBackground(String... params) {
		this.params = params;
		List<NameValuePair> nameValuePairs = setupData();
		if (params.length > 0)
			nameValuePairs.add(new BasicNameValuePair("param", params[0]));
		if (nameValuePairs == null)
			return "";
		return getResponse(nameValuePairs);
	}

	private String getResponse(List<NameValuePair> nameValuePairs) {
		if (context != null && ConnectionChecker.checkConnection(context))
			return DataManager.postData(url, nameValuePairs);
		else
			return "NO CONNECTION";
	}

	public List<NameValuePair> setupData() {
		// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// nameValuePairs.add(new BasicNameValuePair("test", "testdata"));
		return new ArrayList<NameValuePair>();
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public InternetManager(Context context) {
		this.context = context;
	}

}
