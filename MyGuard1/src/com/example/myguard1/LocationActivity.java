package com.example.myguard1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LocationActivity extends Activity {
	private static final String TAG = LocationActivity.class.getSimpleName();
	String location;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.location_layout);
	    Bundle b = getIntent().getExtras();
	    location = b.getString("location");
	    doTheThing();
		}
	    

	private void doTheThing() {
		Log.d(TAG, location);
		TextView dummy=(TextView)findViewById(R.id.textView1);
		dummy.setText(location);
	}
    
}


