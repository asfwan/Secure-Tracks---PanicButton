/****
 * This is the activity that will be started upon the reception of the emergency message.
 * */


package com.sigtech.panicbutton.acts;

import android.app.Activity;
import android.os.Bundle;

import com.sigtech.panicbutton.R;
import com.sigtech.panicbutton.manager.PanicManager;

public class PanicMessageAct extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		PanicManager.setContext(this);
		PanicManager.initiatePanicSound();
	}
}
