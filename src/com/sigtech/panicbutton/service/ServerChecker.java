package com.sigtech.panicbutton.service;

import com.sigtech.panicbutton.manager.internet.ServerStatusManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServerChecker extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		checkServerStatus();
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void checkServerStatus() {
		new ServerStatusManager(this).execute("check_command");
	}

}
