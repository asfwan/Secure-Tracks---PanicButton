/****
 * This is the SMS broadcast receiver. 
 * It listens for an SMS and call certain methods when the pattern matches the emergency message.
 * */

package com.sigtech.panicbutton.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.sigtech.panicbutton.acts.PanicMessageAct;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "SMS received... Analyzing...", Toast.LENGTH_SHORT).show();
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");

			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " :";
				str += msgs[i].getMessageBody().toString();
				str += "\n";
			}

			// ---display the new SMS message---
			if (str.contains("<PB_EMERGENCY_NOTIF!>")) {
				Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
				Intent in = new Intent(context,PanicMessageAct.class);
				in.putExtra("SMS_MESSAGE", str.replace("<PB_EMERGENCY_NOTIF!>", ""));
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(in);
			}
		}
	}
}
