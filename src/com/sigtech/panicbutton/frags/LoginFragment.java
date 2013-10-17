/****
 * This is the Login Fragment. It views the login page and handles the login data.
 * */

package com.sigtech.panicbutton.frags;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.sigtech.panicbutton.R;
import com.sigtech.panicbutton.acts.MainSFAct;
import com.sigtech.panicbutton.manager.ConnectionChecker;
import com.sigtech.panicbutton.manager.DataManager;
import com.sigtech.panicbutton.manager.DatabaseManager;
import com.sigtech.panicbutton.manager.internet.ActivationManager;
import com.sigtech.panicbutton.manager.internet.ContactRetriever;
import com.sigtech.panicbutton.manager.internet.LoginManager;

public class LoginFragment extends SherlockFragment implements OnClickListener {

	// private static final String TAG_LOGIN = "Login-Manager-Fragment";
	static Activity act;
	static EditText password, username;
	static TextView login_notif;
	static public Button login_button;
	static ProgressDialog pd;
	static DatabaseManager dbmgr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataManager.LOGGED_IN = false;
		act = this.getActivity();
		dbmgr = new DatabaseManager(act);
		dbmgr.disableAutoClose();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.login, null);
		username = (EditText) v.findViewById(R.id.username);
		password = (EditText) v.findViewById(R.id.password);
		login_button = (Button) v.findViewById(R.id.login_button);
		login_notif = (TextView) v.findViewById(R.id.loginNotif);

		login_button.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		pd = ProgressDialog.show(act, "Login", "Logging in...");
		String username_S = username.getText().toString(), password_S = password
				.getText().toString();
		new LoginManager((Activity) act).login(username_S, password_S);
	}

	public static void onSuccess(String result) {

		if (DataManager.ALLOW_ACCESS_WITHOUT_LOGIN)
			((MainSFAct) act).mainScreen();

		DataManager.SUCCESS_CODE = "SUCCESS";
		DataManager.LOGGED_IN = true;

		pd.dismiss();

		if (!ConnectionChecker.checkConnection(act)) {
			login_notif.setText("No Internet connection!");
			return;
		}

		boolean loginState = false;
		try {
			loginState = processJSON(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (loginState) {
			((MainSFAct) act).mainScreen();
		} else
			login_notif.setText("Incorrect login info...");

		new ContactRetriever(act).execute();
		new ActivationManager(act).execute();

		// DataManager.showLongToast(act, dbmgr.getInfoAccessToken());
	}

	private static boolean processJSON(String result) throws JSONException {
		JSONObject raw = new JSONObject(result);

		String status = raw.get("status").toString();
		String access_token = raw.getString("access_token");

		JSONObject user_info = raw.getJSONObject("user_info");
		String id_S = user_info.getString("id");
		String email = user_info.getString("email");
		String name = user_info.getString("name");
		String phone = user_info.getString("phone");
		String address = user_info.getString("address");
		String role = user_info.getString("role");
		String registration_date_time = user_info
				.getString("registration_date_time");
		String activation_code = user_info.getString("activation_code");
		String activated_S = user_info.getString("activate");
		String approved_S = user_info.getString("approve");
		int approved = Integer.valueOf(approved_S != null ? approved_S : "0");

		dbmgr.saveUserDetails(id_S, email, name, phone, password.getText()
				.toString(), phone, email, address, role,
				registration_date_time, activation_code, Integer
						.valueOf(activated_S), approved);
		dbmgr.saveInfoStatus(status);
		dbmgr.saveInfoAccessToken(access_token);
		dbmgr.testSavedDetails();
		// DataManager.showLongToast(act, dbmgr.getInfoAccessToken());

		return (approved == 1);
	}
}
