/****
 * This is the login manager. It inherits the Internet Manager and checks the login status with the server.
 * */

package com.sigtech.panicbutton.manager.internet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.util.Log;

import com.sigtech.panicbutton.frags.LoginFragment;
import com.sigtech.panicbutton.manager.DataManager;

public class LoginManager extends InternetManager {
	private static final String TAG = "LOGIN MANAGER";
	Activity act;

	public LoginManager(Activity act) {
		super(act);
		this.act = act;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (result == null)
			return;

		LoginFragment.onSuccess(result);
		if (DataManager.DEBUGGING)
			Log.d(TAG, "LoginManger returns" + result);
	}

	@Override
	public List<NameValuePair> setupData() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("test", "testdata"));
		if (this.params.length > 1) {
			nvp.add(new BasicNameValuePair("email", params[0]));
			nvp.add(new BasicNameValuePair("password", params[1]));
		}

		return nvp;
	}

	public void login(String email, String password) {
		this.url = DataManager.URL_LOGIN;
		execute(email, password);
	}

}
