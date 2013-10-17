/****
 * This is the Unlocker Fragment. It appears when the Panic Flag is true. 
 * It unlocks when it gets the right password.
 * */

package com.sigtech.panicbutton.frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.sigtech.panicbutton.R;
import com.sigtech.panicbutton.acts.MainSFAct;
import com.sigtech.panicbutton.manager.DataManager;
import com.sigtech.panicbutton.manager.DatabaseManager;

public class UnlockerFragment extends SherlockFragment implements
		OnClickListener {

	private static String PASSWORD = null;

	public UnlockerFragment() {
		DataManager.PREVENT_BACK_BUTTON = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.unlockscreen, null);
		setupPassword();
		setupViews(v);
		return v;
	}

	private void setupPassword() {
		PASSWORD = new DatabaseManager(getActivity()).getInfoPIN();
	}

	Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bdel, benter;
	TextView title, panel;
	String passwd = "";

	private void setupViews(View v) {
		b1 = (Button) v.findViewById(R.id.b1);
		b2 = (Button) v.findViewById(R.id.b2);
		b3 = (Button) v.findViewById(R.id.b3);
		b4 = (Button) v.findViewById(R.id.b4);
		b5 = (Button) v.findViewById(R.id.b5);
		b6 = (Button) v.findViewById(R.id.b6);
		b7 = (Button) v.findViewById(R.id.b7);
		b8 = (Button) v.findViewById(R.id.b8);
		b9 = (Button) v.findViewById(R.id.b9);
		b0 = (Button) v.findViewById(R.id.b0);
		bdel = (Button) v.findViewById(R.id.bdel);
		benter = (Button) v.findViewById(R.id.benter);

		panel = (TextView) v.findViewById(R.id.panel);
		title = (TextView) v.findViewById(R.id.unlocker_title);

		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);
		b5.setOnClickListener(this);
		b6.setOnClickListener(this);
		b7.setOnClickListener(this);
		b8.setOnClickListener(this);
		b9.setOnClickListener(this);
		b0.setOnClickListener(this);
		bdel.setOnClickListener(this);
		benter.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b1:
			passwd += "1";
			break;
		case R.id.b2:
			passwd += "2";
			break;
		case R.id.b3:
			passwd += "3";
			break;
		case R.id.b4:
			passwd += "4";
			break;
		case R.id.b5:
			passwd += "5";
			break;
		case R.id.b6:
			passwd += "6";
			break;
		case R.id.b7:
			passwd += "7";
			break;
		case R.id.b8:
			passwd += "8";
			break;
		case R.id.b9:
			passwd += "9";
			break;
		case R.id.b0:
			passwd += "0";
			break;
		case R.id.bdel:
			int length = passwd.length();
			if (length > 0) {
				passwd = (passwd.substring(0, length - 1));
			}
			break;
		case R.id.benter:
			execEnter();
			break;
		}
		syncPanel();
	}

	private void syncPanel() {
		int length = passwd.length();
		String asterisks = "";
		for (int i = 0; i < length; i++) {
			asterisks += "*";
		}
		panel.setText(asterisks);
		if (passwd.equals(PASSWORD)) {
			((MainSFAct) this.getActivity()).mBoundService.notifyUnlocked();
			return;
		}
		if (passwd.length() >= PASSWORD.length()) {
			Toast.makeText(this.getActivity(), "Wrong code!" + passwd,
					Toast.LENGTH_LONG).show();
			passwd = "";
		}
	}

	private void execEnter() {
		if (passwd.equals(PASSWORD)) {
			// DataManager.settleDownPanic(this.getActivity());
			((MainSFAct) this.getActivity()).mBoundService.notifyUnlocked();
		} else
			Toast.makeText(this.getActivity(), "Wrong code!" + passwd,
					Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDestroyView() {
		DataManager.PREVENT_BACK_BUTTON = false;
		super.onDestroyView();
	}

}
