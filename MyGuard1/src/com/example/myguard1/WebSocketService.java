package com.example.myguard1;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class WebSocketService extends Service {

	private static final String TAG = WebSocketService.class.getSimpleName();
	SharedPreferences prefs;
	private static final String wsuri = "ws://192.168.12.1:9000";
	// private static final String wsuri ="ws://connect.mysensors.mobi:8080";
	WebSocketConnection wsConnection;
	public Location location;
	MyGuardApplication application;

	@Override
	public void onCreate() {

		super.onCreate();
		application = (MyGuardApplication) getApplication();
		wsConnection = ((MyGuardApplication) getApplication())
				.getWsConnection();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		wsConnect();
		Toast.makeText(this, "WS service initiated", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "service started");
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		wsConnection.disconnect();
		Toast.makeText(this, "WS service stopped", Toast.LENGTH_SHORT).show();

		Log.d(TAG, "service destroyed");
	}

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	public void wsConnect() {

		try {
			wsConnection.connect(wsuri, new WebSocketHandler() {
				@Override
				public void onOpen() {
					Log.d(TAG, "Status: Connected to " + wsuri);
				}

				@Override
				public void onTextMessage(String payload) {

					Log.d(TAG, "Message recieved: " + payload);
					QueryHandler.handleQuery(application, payload);

				}

				@Override
				public void onBinaryMessage(byte[] payload) {

					Log.d(TAG, "Message recieved: " + payload);
					QueryHandler.handleQuery(application, payload);

				}

				@Override
				public void onClose(int code, String reason) {
					Log.d(TAG, "Connection lost: " + reason);
				}
			});
		} catch (WebSocketException e) {

			Log.d(TAG, e.toString());
		}
	}

}
