/****
 * This is the SMS Manager. It stores methods for SMS sending.
 * */

package com.sigtech.panicbutton.manager;

import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMSManager {
	private static final String ACTION_SMS_SENT = "com.sigtech.panicbutton.SMS_SENT_ACTION";

	public static void sendSMS(Context context, String SMS_Number,
			String message) {

		SmsManager sms = SmsManager.getDefault();

		List<String> messages = sms.divideMessage(message);

		for (String msg : messages) {
			if (DataManager.SMS_DEBUGGING) {
				Toast.makeText(context, SMS_Number + " " + msg,
						Toast.LENGTH_LONG).show();
			} else {
				sms.sendTextMessage(SMS_Number, null, msg, PendingIntent
						.getBroadcast(context, 0, new Intent(ACTION_SMS_SENT),
								0), null);

				Toast.makeText(context, "Sent to: " + SMS_Number,
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
