package com.example.myfirstapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class ListenToServer extends Service {
	private static final String TAG = "ListenToService";
	private final WebSocketConnection mConnection = new WebSocketConnection();

	@Override
	public void onCreate() {

		Thread thread = new Thread() {
			@Override
			public void run() {
				Log.d(TAG, "Service started.");
				String userName = "u1";
				String passwd = "1234";
				final String message = "LOGIN #name " + userName + " #skey " + passwd
						+ " @mysensors\n";
				final String shareMessage = "SHARE #dummySensor @u2";
				final String wsuri = "ws://192.168.12.1:9000";
				try {
					mConnection.connect(wsuri, new WebSocketHandler() {
						@Override
						public void onOpen() {
							Log.d(TAG, "Status: Connected to " + wsuri);
							mConnection.sendTextMessage(message);
							mConnection.sendTextMessage(shareMessage);
						}

						@Override
						public void onTextMessage(String payload) {
							parseMessage(payload);
						}

						@Override
						public void onClose(int code, String reason) {
							Log.d(TAG, "Connection lost.");
						}
					});
				} catch (WebSocketException e) {

					Log.d(TAG, e.toString());
				}
				Log.d(TAG, "connection made.");

			}
		};
		thread.start();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void makeConnection() {
		String userName = "u1";
		String passwd = "1234";
		final String message = "LOGIN #name " + userName + " #skey " + passwd
				+ " @mysensors\n";
		final String shareMessage = "SHARE #dummySensor @u2";
		final String wsuri = "ws://192.168.12.1:9000";
		try {
			mConnection.connect(wsuri, new WebSocketHandler() {
				@Override
				public void onOpen() {
					Log.d(TAG, "Status: Connected to " + wsuri);
					mConnection.sendTextMessage(message);
					mConnection.sendTextMessage(shareMessage);
				}

				@Override
				public void onTextMessage(String payload) {
					parseMessage(payload);
				}

				@Override
				public void onClose(int code, String reason) {
					Log.d(TAG, "Connection lost.");
				}
			});
		} catch (WebSocketException e) {

			Log.d(TAG, e.toString());
		}

	}

	public void parseMessage(String message) {
		Log.d(TAG, "Server said: " + message);
	}

	
	/*
	 * @Override public void onDestroy() { // Cancel the persistent
	 * notification. mNM.cancel(R.string.local_service_started);
	 * 
	 * // Tell the user we stopped. Toast.makeText(this,
	 * R.string.local_service_stopped, Toast.LENGTH_SHORT).show(); }
	 */

}