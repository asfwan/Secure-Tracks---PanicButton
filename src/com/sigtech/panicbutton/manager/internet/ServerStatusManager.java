package com.sigtech.panicbutton.manager.internet;

import com.sigtech.panicbutton.manager.DataManager;
import com.sigtech.panicbutton.service.MainService;

import android.content.Context;
import android.widget.Toast;

public class ServerStatusManager extends InternetManager {
	public ServerStatusManager(Context context) {
		super(context);
		this.url = "http://malaysianvote.com/panicbutton/";
		this.context = context;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
//		if(DataManager.DEBUGGING)showToast("ServerStatus: " + result);
		DataManager.SERVER_STATUS=result;
		MainService.notifyServerStatusChecked();
	}

	public void showToast(String txt) {
		Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
	}
}
