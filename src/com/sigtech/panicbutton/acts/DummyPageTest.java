package com.sigtech.panicbutton.acts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sigtech.panicbutton.R;

public class DummyPageTest extends Activity {

	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		Intent intent = getIntent();
		String data = intent.getStringExtra("data");

		tv = (TextView) findViewById(R.id.dummy_text);

		tv.setText(data);
	}

}
