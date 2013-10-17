/****
 * This is the Splash Screen for this app.
 * */

package com.sigtech.panicbutton.acts;

import com.sigtech.panicbutton.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class SplashAct extends Activity {
	Handler h;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				finish();
				super.handleMessage(msg);
			}
		};

		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				h.sendEmptyMessage(0);
			}
		}, 3000);
	}

}
