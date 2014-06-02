package com.example.myguard1;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import de.tavendo.autobahn.WebSocketConnection;

public class MyGuardApplication extends Application implements
OnSharedPreferenceChangeListener {
	public final static String WEB_SOCKET_URI = "ws://connect.mysensors.mobi:8080";

	SharedPreferences prefs;
	public WebSocketConnection wsConnection;
	private String username;
	private String password;
	//	public Location location;


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		username = prefs.getString("username", "");
		password = prefs.getString("password", "");
	}

	public WebSocketConnection getWsConnection() {
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
		wsConnection = null;
	}
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
}