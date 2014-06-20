package com.example.myguard1;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class RecorderService extends Service {
	private static final String TAG = "RecorderService";
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private static Camera mServiceCamera;
	private boolean mRecordingStatus;
	private MediaRecorder mMediaRecorder;
	MyGuardApplication application;

	@Override
	public void onCreate() {
		super.onCreate();
		mRecordingStatus = false;
		mServiceCamera = CameraRecorder.mCamera;
		mSurfaceView = CameraRecorder.mSurfaceView;
		mSurfaceHolder = CameraRecorder.mSurfaceHolder;		
		application = ((MyGuardApplication) getApplication());
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d("Namil Recoder", "Start r12321313ing");
		if (mRecordingStatus == false)
			startRecording();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		stopRecording();
		mRecordingStatus = false;

		super.onDestroy();
	}

	public boolean startRecording() {
		try {
			Log.d("Namil Recoder", "Start recoding");

			// Toast.makeText(getBaseContext(), "Recording Started",
			// Toast.LENGTH_SHORT).show();

			mServiceCamera = Camera.open();
			Camera.Parameters params = mServiceCamera.getParameters();
			mServiceCamera.setParameters(params);
			Camera.Parameters p = mServiceCamera.getParameters();
			Log.d("Namil Recoder", "Start recoding1");

			final List<Size> listSize = p.getSupportedPreviewSizes();
			Log.d("Namil Recoder", "Start recoding2" + listSize.size());
			Size mPreviewSize = listSize.get(0);
			/*
			 * Log.v(TAG, "use: width = " + mPreviewSize.width + " height = " +
			 * mPreviewSize.height);
			 */

			p.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			p.setPreviewFormat(PixelFormat.YCbCr_420_SP);
			mServiceCamera.setParameters(p);

			try {
				mServiceCamera.setPreviewDisplay(mSurfaceHolder);
				mServiceCamera.startPreview();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}

			mServiceCamera.unlock();

			mMediaRecorder = new MediaRecorder();
			mMediaRecorder.setCamera(mServiceCamera);
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
			
			File mediaStorageDir = new File("/sdcard/Surveillance_namil/");
		    if ( !mediaStorageDir.exists() ) {
		        if ( !mediaStorageDir.mkdirs() ){
		            Log.d("failed to create directory","namil recorder");
		        	}
		    	}
//		    String filePath = "/sdcard/Surveillance_namil/" + "VID_"+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
		    application.setVideoLink();
		    String filePath = application.getVideoLink();
		    mMediaRecorder.setOutputFile(filePath);
			//mMediaRecorder.setOutputFile("/sdcard/video.mp4");
			mMediaRecorder.setVideoFrameRate(30);
			mMediaRecorder.setVideoSize(mPreviewSize.width, mPreviewSize.height);
			mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

			mMediaRecorder.prepare();
			Log.d("Namil Recoder", "Initialization finished");
			mMediaRecorder.start();
			Log.d("Namil Recoder", "Recodig started");

			mRecordingStatus = true;

			return true;
		} catch (IllegalStateException e) {
			Log.d(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public void stopRecording() {
		
		Toast.makeText(getBaseContext(), "Recording Stopped",
				Toast.LENGTH_SHORT).show();
		try {
			mServiceCamera.reconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediaRecorder.stop();
		mMediaRecorder.reset();

		mServiceCamera.stopPreview();
		mMediaRecorder.release();

		mServiceCamera.release();
		mServiceCamera = null;
		String fname=application.getVideoLink();
		Log.d(TAG, "started uploading");
		application.uploadToDB(fname, fname);
		Log.d(TAG, "finished uploading");
	}
}
