package com.example.myguard1;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CameraRecorder extends Activity implements SurfaceHolder.Callback {

	private static final String TAG = "Recorder";
	public static SurfaceView mSurfaceView;
	public static SurfaceHolder mSurfaceHolder;
	public static Camera mCamera;
	public static boolean mPreviewRunning;
	private Timer mStopTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_recorder);
		/**
		 * This is normal statements for an activity
		 * */
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// initializing 2 buttons, Start recording and stop recording.

		Button btnStart = (Button) findViewById(R.id.StartService);
		btnStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Create new service using CameraRecorder class and Start
				// service
				Intent intent = new Intent(CameraRecorder.this,
						RecorderService.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startService(intent);
				finish();
			}
		});

		Button btnStop = (Button) findViewById(R.id.StopService);
		btnStop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Stop CameraRecorder Service
				// stopService(new Intent(CameraRecorder.this,RecorderService.class));

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), RecorderService.class);
				getApplicationContext().stopService(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera_recorder, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_camera_recorder,
					container, false);
			return rootView;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		// Create new service using CameraRecorder class and Start
		// service
		Intent intent = new Intent(CameraRecorder.this, RecorderService.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(intent);
		mStopTimer = new Timer();
		mStopTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				stopService(new Intent(CameraRecorder.this,RecorderService.class));
			}
		}, 60 * 1000);
		finish();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

}
