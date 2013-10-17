/****
 * This is the Form Fragment. It creates the form when the user launches this app for the first time..
 * */

package com.sigtech.panicbutton.frags;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.sigtech.panicbutton.R;
import com.sigtech.panicbutton.acts.MainSFAct;
import com.sigtech.panicbutton.manager.DatabaseManager;

public class FormFragment extends SherlockFragment implements OnClickListener {
	Activity act;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		act = this.getActivity();
		View v = inflater.inflate(R.layout.input_details, null);

		button_submit = (Button) v.findViewById(R.id.button_submit);

		username = (EditText) v.findViewById(R.id.et_username);
		panic_contact = (EditText) v.findViewById(R.id.et_panic_contact);
		email_contact = (EditText) v.findViewById(R.id.et_email);
		address = (EditText) v.findViewById(R.id.et_address);
		password = (EditText) v.findViewById(R.id.password);

		button_submit.setOnClickListener(this);

		return v;
	}

	EditText username, panic_contact, email_contact, address, password;
	Button button_submit;

	@Override
	public void onClick(View v) {
		String uname = username.getText().toString();
		String pwd = password.getText().toString();
		String pcontact = panic_contact.getText().toString();
		String emlcontact = email_contact.getText().toString();
		String addr = address.getText().toString();

		if (uname.equals("") || pwd.equals("") || pcontact.equals("")
				|| emlcontact.equals("") || addr.equals("")) {
			Toast.makeText(act, "All fields are required!", Toast.LENGTH_LONG)
					.show();
			return;
		}
		// saveUserDetails(String username, String name, String phone,
		// String password, String panic_num, String email, String address,
		// String role, String registration_date_time, String activation_code,
		// int activated, int approved)
		new DatabaseManager(act).saveUserDetails("0",uname, uname, pcontact, pwd,
				pcontact, emlcontact, addr, null, "date_time", "code", 1, 1);
		new DatabaseManager(act).testSavedDetails();
		new DatabaseManager(act);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					((MainSFAct) act).mainScreen();
				}
			}
		});
		t.run();
	}
}
