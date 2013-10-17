/****
 * This is the Panic Manager. It stores the methods to be used for Panic Mode.
 * */

package com.sigtech.panicbutton.manager;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

public class PanicManager {
	static Context c = null;
	public static MediaPlayer mPlayer = null;
	static Vibrator v;
	static DatabaseManager dbmgr;

	public static void onPanicStopped(Context c2, String lat, String lng) {
		if (DataManager.VIBRATOR_ENABLED)
			v.cancel();
		stopPanicSound();
		SMSManager.sendSMS(c2, DataManager.SERVER_SMS_NUMBER,
				"TM ICONST {\"user_id\":\"" + dbmgr.getId()
						+ "\",\"access_token\":\"" + dbmgr.getInfoAccessToken()
						+ "\",\"emergency\":"
						+ (DataManager.TRIGGERING ? "true" : "false")
						+ ",\"latitude\":" + lat + ",\"longitude\":" + lng
						+ "}");
	}

	private static void onPanicStarted() {
		if (DataManager.VIBRATOR_ENABLED) {
			v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
			new Thread() {
				@Override
				public void run() {
					while (DataManager.PANICKED) {
						v.vibrate(1000);
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

		}
	}

	public static void stopPanicSound() {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
		}

	}

	public static void initiatePanic(Context c2, String lat, String lng) {
		c = c2;
		if (lat.contains(":"))
			lat = lat.split(":")[1];
		if (lng.contains(":"))
			lng = lng.split(":")[1];
		if (DataManager.PANIC_SOUND_ENABLED)
			initiatePanicSound();
		if (DataManager.SMS_SEND_ENABLED) {
			initiatePanicSMSSending(lat, lng);
		}
		onPanicStarted();
	}

	private static void initiatePanicSMSSending(String lat, String lng) {
		dbmgr = new DatabaseManager(c);
		dbmgr.disableAutoClose();
		DataManager.showLongToast(c, dbmgr.getInfoAccessToken());
		if (!DataManager.SEND_TO_SERVER) {
			String[] number_to_send = new DatabaseManager(c).getPanicNums();

			// Toast.makeText(this, "Sending SMS to Panic num: " +
			// number_to_send,
			// Toast.LENGTH_LONG).show();

			if (number_to_send == null) {
				Toast.makeText(c, "Panic Number was not found!",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (DataManager.SMS_SEND_ENABLED) {
				for (int i = 0; i < number_to_send.length; i++)
					if (DataManager.check_valid_number(number_to_send[i]))
						SMSManager.sendSMS(
								c,
								"+6" + number_to_send[i],
								"TM ICONST {\"user_id\":\""
										+ dbmgr.getId()
										+ "\",\"access_token\":\""
										+ dbmgr.getInfoAccessToken()
										+ "\",\"emergency\":"
										+ (DataManager.TRIGGERING ? "true"
												: "false") + ",\"latitude\":"
										+ lat + ",\"longitude\":" + lng + "}");

				// {\"user_id\":\"5211C65A21BF2\",\"access_token\":\""++"\",\"emergency\":true,\"latitude\".045149,\"longitude\":101.618668}
			}
		} else if (DataManager.SMS_SEND_ENABLED) {
			// if
			// (DataManager.check_valid_number(DataManager.SERVER_SMS_NUMBER))
			SMSManager.sendSMS(
					c,
					DataManager.SERVER_SMS_NUMBER,
					"TM ICONST {\"user_id\":\"" + dbmgr.getId()
							+ "\",\"access_token\":\""
							+ dbmgr.getInfoAccessToken() + "\",\"emergency\":"
							+ (DataManager.TRIGGERING ? "true" : "false")
							+ ",\"latitude\":" + lat + ",\"longitude\":" + lng
							+ "}");
		}

	}

	public static void initiatePanicSound() {
		if (c == null)
			return;
		if (DataManager.PANICKED) {
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(
						c,
						Uri.parse("android.resource://com.sigtech.panicbutton/raw/siren2"));
			} catch (Exception e) {
				e.printStackTrace();
			}

			mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			// mPlayer.setVolume(90, 90);
			try {
				mPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mPlayer.setLooping(true);

			mPlayer.start();
			DataManager.PANICKED = true;
		} else {
			if (mPlayer != null) {
				mPlayer.stop();
				mPlayer.release();
			}
		}

	}

	public static void setContext(Context c) {
		PanicManager.c = c;
	}

}
