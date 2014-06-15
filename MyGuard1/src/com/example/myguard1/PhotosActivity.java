package com.example.myguard1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PhotosActivity extends Activity {
	private static final String TAG = LocationActivity.class.getSimpleName();
	String photoLink;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_layout);
        Bundle b = getIntent().getExtras();
        photoLink = b.getString("photo");
        doTheThing();
		}
	    

	private void doTheThing() {
		Log.d(TAG, photoLink);
		
		TextView dummy=(TextView)findViewById(R.id.textView1);
		dummy.setText(photoLink);
	}
}
