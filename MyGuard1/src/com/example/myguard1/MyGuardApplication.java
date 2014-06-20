package com.example.myguard1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

import de.tavendo.autobahn.WebSocketConnection;

public class MyGuardApplication extends Application implements
OnSharedPreferenceChangeListener {
	private static final String TAG = MyGuardApplication.class.getSimpleName();
	// public final static String WEB_SOCKET_URI =
	// "ws://connect.mysensors.mobi:8080";

	SharedPreferences prefs;
	public WebSocketConnection wsConnection;
	private String username;
	private String password;
	private String pairedDev;
	private String location;
	private String photoLink;
	private String videoLink;
	private String audioLink;
	private Boolean serviceRunning;
	private Boolean userLogged;
	private DropboxAPI<AndroidAuthSession> mDBApi;
	
	//fname="/sdcard/DCIM/Camera/qw.jpg"
	//fnameDB=qw.jpg
	public String getVideoLink(){
		return videoLink;
	}
	public void setVideoLink(){
		videoLink = "/sdcard/Surveillance_namil/" + "VID_"+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";

	}
	public void uploadToDB(String fname,String fnameDB) {

		try {
			Entry response;
			File file = new File(fname);
			FileInputStream inputStream = new FileInputStream(file);
			Log.d(TAG, "uploading");
			response = mDBApi.putFile(fnameDB, inputStream, file.length(),
					null, null);
			Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//fname="/sdcard/DCIM/Camera/qw1.jpg"
	public void downloadFromDB(String fnameL,String fnameDB){
		try {
			File file = new File(fnameL);
			FileOutputStream outputStream = new FileOutputStream(file);
			DropboxFileInfo info = mDBApi.getFile(fnameDB, null, outputStream, null);
			Log.i("DbExampleLog", "The file's rev is: " + info.getMetadata().rev);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DropboxAPI<AndroidAuthSession> getDBApi() {
		return mDBApi;
	}

	public void setDBApi(DropboxAPI<AndroidAuthSession> myMDBApi) {
		mDBApi = myMDBApi;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		username = prefs.getString("username", "u1");
		password = prefs.getString("password", "1234");
		pairedDev = prefs.getString("pairedDev", "u2");
		/*
		 * location=username+"'s_location_val";
		 * photoLink=username+"'s_photolink_val";
		 * videoLink=username+"'s_videolink_val";
		 * audioLink=username+"'s_audiolink_val";
		 */
		serviceRunning = false;
		userLogged = false;
	}

	public WebSocketConnection getWsConnection() {
		// Log.d(TAG, "wsConnectio created and returned");
		if (wsConnection == null) {
			wsConnection = new WebSocketConnection();
		}
		return wsConnection;
	}

	// call when preferences changed
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// invalidate wsConnection
		/*
		 * Log.d(TAG, "prefrences changed.wsConnection nullfied"); wsConnection
		 * = null;
		 */
		Toast.makeText(this, "Restart the app to make your changes effective",
				Toast.LENGTH_LONG).show();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getPairedDev() {
		return pairedDev;
	}

	public void setAudio(String loc) {
		audioLink = loc;
	}

	public String getAudio() {
		return audioLink;
	}

	public void setVideo(String loc) {
		videoLink = loc;
	}

	public String getVideo() {
		return videoLink;
	}

	public void setPhoto(String loc) {
		photoLink = loc;
	}

	public String getPhoto() {
		return photoLink;
	}

	public void setLocation(String loc) {
		location = loc;
	}

	

	public void startService() {
		startService(new Intent(this, WebSocketService.class));
		serviceRunning = true;
	}

	public void stopService() {
		stopService(new Intent(this, WebSocketService.class));
		serviceRunning = false;
	}

	public boolean serviceRunning() {
		return serviceRunning;
	}

	public boolean userLogged() {
		return userLogged;
	}

	public void sendLoginMessege() {
		wsConnection.sendTextMessage("LOGIN #name " + username + " #skey "
				+ password + " @mysensors\n");
		userLogged = true;
	}
	public void startVideoService(){
		final Intent intent = new Intent(this, CameraRecorder.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		
	}
	public void setLocation(){
		GPSTracker gps = new GPSTracker(this);
        double lng=gps.getLongitude();
        double lat=gps.getLatitude();
		location=Double.toString(lng)+","+Double.toString(lat);
        
	}
	public String getLocation(){
		return location;
	}

}