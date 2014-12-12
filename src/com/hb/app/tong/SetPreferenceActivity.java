package com.hb.app.tong;


import android.app.Activity;
import android.os.Bundle;

public class SetPreferenceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_preference);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();
	}

}
