package com.android.camerarecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
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

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	@Override
	public void onCreate() {
		mRecordingStatus = false;
		mServiceCamera = CameraRecorder.mCamera;
		mSurfaceView = CameraRecorder.mSurfaceView;
		mSurfaceHolder = CameraRecorder.mSurfaceHolder;

		super.onCreate();
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
		if (mRecordingStatus == false){
			startRecording();
			startTimerRecord();
		}
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
				mServiceCamera.setPreviewCallback(new PreviewCallback() {

					@Override
					public void onPreviewFrame(byte[] arg0, Camera arg1) {
						// TODO Auto-generated method stub
						Log.d("namil-preview cam", "byte aarrrrrrrrrrrrrrrrrrr");
					}
				});
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
			mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO)
					.toString());
			// mMediaRecorder.setOutputFile("/sdcard/video.mp4");
			mMediaRecorder.setVideoFrameRate(30);
			mMediaRecorder
					.setVideoSize(mPreviewSize.width, mPreviewSize.height);
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
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"NamilCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	TimerTask scanTask;
	final Handler handler = new Handler();
	Timer t = new Timer();
	int counter = 0;

	public void startTimerRecord() {

		scanTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						// TextView tw=(TextView) findViewById(R.id.textView1);
						// tw.setText("time: "+counter);
						counter++;
						stopRecording();
						startRecording();
						Log.d("TIMER", "Timer set off");
					}
				});
			}
		};

		t.schedule(scanTask, 300, 60000);

	}

	public void stopTimerRecord() {

		if (scanTask != null) {
			Log.d("TIMER", "timer canceled");
			scanTask.cancel();
			stopRecording();
		}

	}
}
