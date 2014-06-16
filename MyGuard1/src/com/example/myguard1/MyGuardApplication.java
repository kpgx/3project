package com.example.myguard1;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.widget.Toast;
import de.tavendo.autobahn.WebSocketConnection;

public class MyGuardApplication extends Application implements
OnSharedPreferenceChangeListener {
	private static final String TAG = MyGuardApplication.class.getSimpleName();
	//public final static String WEB_SOCKET_URI = "ws://connect.mysensors.mobi:8080";

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
//	public String log;
	


	@Override
	public void onCreate() {		
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		username = prefs.getString("username", "");
		password = prefs.getString("password", "");
		pairedDev=prefs.getString("pairedDev", "");
		/*location=username+"'s_location_val";
		photoLink=username+"'s_photolink_val";
		videoLink=username+"'s_videolink_val";
		audioLink=username+"'s_audiolink_val";*/
		serviceRunning=false;
		userLogged=false;
	}

	public WebSocketConnection getWsConnection() {
//		Log.d(TAG, "wsConnectio created and returned");
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
		/*Log.d(TAG, "prefrences changed.wsConnection nullfied");
		wsConnection = null;*/
		Toast.makeText(this, "Restart the app to make your changes effective", Toast.LENGTH_LONG).show();
	}
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
	public String getPairedDev(){
		return pairedDev;
	}
	public void setAudio(String loc){
		audioLink=loc;
	}
	public String getAudio(){
		return audioLink;
	}
	public void setVideo(String loc){
		videoLink=loc;
	}
	public String getVideo(){
		return videoLink;
	}
	public void setPhoto(String loc){
		photoLink=loc;
	}
	public String getPhoto(){
		return photoLink;
	}
	public void setLocation(String loc){
		location=loc;
	}
	public String getLocation(){
		return location;
	}
	public void startService(){
		startService(new Intent(this, WebSocketService.class));
		serviceRunning=true;
	}
	public void stopService(){
	stopService(new Intent(this, WebSocketService.class));
	serviceRunning=false;
	}
	public boolean serviceRunning(){
		return serviceRunning;
	}
	public boolean userLogged(){
		return userLogged;
	}
	public void sendLoginMessege(){
		wsConnection.sendTextMessage("LOGIN #name " + username + " #skey "
				+ password + " @mysensors\n");
		userLogged =true;
	}
}