/****
 * This is the About Fragment. It shows the About of this application as well as other information.
 * */

package com.sigtech.panicbutton.frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.sigtech.panicbutton.R;

public class AboutFragment extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// DataManager.checkLogin(this);
		return inflater.inflate(R.layout.about, null);
		// MainActivity.initiatePanicSound();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

}
