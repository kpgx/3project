package com.example.myguard1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class VideosActivity extends Activity {
	private static final String TAG = LocationActivity.class.getSimpleName();
	String videoLink;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_layout);
        Bundle b = getIntent().getExtras();
        videoLink = b.getString("video");
        doTheThing();
		}
	    

	private void doTheThing() {
		Log.d(TAG, videoLink);
		TextView dummy=(TextView)findViewById(R.id.textView1);
		dummy.setText(videoLink);
	}
}