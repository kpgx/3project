package com.example.myguard1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AudioActivity extends Activity {
	private static final String TAG = AudioActivity.class.getSimpleName();
	String audioLink;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_layout);
        Bundle b = getIntent().getExtras();
        audioLink = b.getString("audio");
        doTheThing();
		}
	    

	private void doTheThing() {
		Log.d(TAG, audioLink);
		TextView dummy=(TextView)findViewById(R.id.textView1);
		dummy.setText(audioLink);
	}
}