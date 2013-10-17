/****
 * This is the Data Manager. It is where the most static data are located.
 * */

package com.sigtech.panicbutton.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.sigtech.panicbutton.acts.SplashAct;
import com.sigtech.panicbutton.frags.LoginFragment;
import com.sigtech.panicbutton.frags.MainPanicFragment;

public class DataManager {
	public static final boolean DEBUGGING = false;
	public static final boolean SMS_DEBUGGING = false;
	public static final boolean ALLOW_ACCESS_WITHOUT_LOGIN = false;
	public static final boolean PANIC_SOUND_ENABLED = false;
	public static boolean VIBRATOR_ENABLED = true;
	public static boolean SMS_SEND_ENABLED = false;
	public static final String URL_UPDATE_LOCATION_1 = "http://retail-dev.icon2u.com.my/index.php/secure_tracks/api/users/";
	public static final String URL_UPDATE_LOCATION_2 = "/location";
	public static final String URL_ACTIVATION_1 = "http://retail-dev.icon2u.com.my/index.php/secure_tracks/api/users/";
	public static final String URL_ACTIVATION_2 = "/activate";
	public static final String URL_UPDATE_LOCATION_EMERGENCY_3 = "/emergency";
	public static final String URL_RETRIEVE_CONTACT_1 = "http://retail-dev.icon2u.com.my/index.php/secure_tracks/api/users/";
	public static final String URL_RETRIEVE_CONTACT_2 = "/permission/grantedto";
	public static final boolean SEND_TO_SERVER = true;
	public static final String SERVER_SMS_NUMBER_TEST = "0173796878";
	public static final String SERVER_SMS_NUMBER = "36660";

	public static boolean TRIGGERING = true;
	public static String URL_LOGIN = "http://retail-dev.icon2u.com.my/index.php/user/api/users/mlogin";
	public static String URL_LOGOUT_1 = "http://retail-dev.icon2u.com.my/index.php/user/api/users/";
	public static String URL_LOGOUT_2 = "/mlogout";
	public static boolean LATLNG_SET_EXACT = false;
	public static boolean MAIN_IS_OPEN = false;
	public static boolean ON_MAIN_PANIC_SCREEN = false;
	public static boolean PREVENT_BACK_BUTTON = false;
	public static boolean PANICKED = false, LATLNG_SET = false,
			LOGGED_IN = false;
	public static String SUCCESS_CODE;
	public static String SERVER_STATUS = "UNCHECKED";
	public static boolean PLAY_SERVICES_AVAILABLE = false;
	public static double longitude = -1;
	public static double latitude = -1;

	public static String getData(String url) {
		String line = null;
		StringBuilder sb = new StringBuilder();

		try {
			URL urlCOnn = new URL(url);
			InputStream is = urlCOnn.openConnection().getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String postData(String url, List<NameValuePair> nameValuePairs) {
		String resp = null;
		StringBuilder sb = new StringBuilder();

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		if (url == null)
			url = "http://asfwan.dlinkddns.com/test/";
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data - example
			// nameValuePairs = new ArrayList<NameValuePair>(2);
			// nameValuePairs.add(new BasicNameValuePair("test", "testdata"));

			// setting namevaluepairs
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			while ((resp = reader.readLine()) != null) {
				sb.append(resp);
				if (DEBUGGING)
					Log.d("resp", resp);
			}

			resp = sb.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resp;
	}

	public static boolean check_valid_number(String number_to_send) {
		return number_to_send.length() >= 6;
	}

	public static void logOut(Activity act) {
		act.startActivity(new Intent(act, LoginFragment.class));
		act.finish();
	}

	public static void checkLogin(Activity act) {
		if (!DataManager.LOGGED_IN)
			logOut(act);
	}

	public static void checkAndProceedToMain(Activity act) {
		if (DataManager.LOGGED_IN) {
			act.startActivity(new Intent(act, MainPanicFragment.class));
			act.finish();
		} else
			showSplash(act);
	}

	public static void showSplash(Activity act) {
		act.startActivity(new Intent(act, SplashAct.class));
	}

	public static void showToast(Context ctxt, String txt) {
		Toast.makeText(ctxt, txt, Toast.LENGTH_SHORT).show();
	}

	public static void showLongToast(Context ctxt, String txt) {
		Toast.makeText(ctxt, txt, Toast.LENGTH_LONG).show();
	}

	// public static String putData(String url1, String data) {
	// String line = "";
	// StringBuilder sb = new StringBuilder();
	// try {
	// URL url = new URL(url1);
	// HttpURLConnection httpCon = (HttpURLConnection) url
	// .openConnection();
	// httpCon.setDoOutput(true);
	// httpCon.setDoInput(true);
	// httpCon.setRequestMethod("PUT");
	// // httpCon.setRequestProperty("Content-Type",
	// // "application/x-www-form-urlencoded");
	// DataOutputStream out = new DataOutputStream(
	// httpCon.getOutputStream());
	// out.writeBytes(data);
	// out.flush();
	// out.close();
	// httpCon.connect();
	// InputStream is = httpCon.getInputStream();
	//
	// BufferedReader reader = new BufferedReader(new InputStreamReader(
	// is, "iso-8859-1"), 8);
	// reader.close();
	// while ((line = reader.readLine()) != null) {
	// sb.append(line);
	// }
	//
	// return sb.toString();
	//
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (ProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	public static String putData(String url, List<NameValuePair> nameValuePairs) {
		String resp = null;
		StringBuilder sb = new StringBuilder();

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		if (url == null)
			url = "http://asfwan.dlinkddns.com/test/";
		HttpPut httpput = new HttpPut(url);

		try {
			// setting namevaluepairs
			httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httpput);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			while ((resp = reader.readLine()) != null) {
				sb.append(resp);
				if (DEBUGGING)
					Log.d("resp", resp);
			}

			resp = sb.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resp;
	}

	public static String deleteData(String url1) {
		String line = "";
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(url1);
			HttpURLConnection httpCon = (HttpURLConnection) url
					.openConnection();
			httpCon.setRequestMethod("DELETE");
			InputStream is = httpCon.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
