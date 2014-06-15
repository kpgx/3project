package com.example.myguard1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import de.tavendo.autobahn.WebSocketConnection;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	WebSocketConnection wsConnection;
	MyGuardApplication application;
	// preferences
	SharedPreferences prefs;
	private String username;
	private String password;
	private String pairedDev;
	private long DELAY = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			application = ((MyGuardApplication) getApplication());
			wsConnection = ((MyGuardApplication) getApplication())
					.getWsConnection();
			prefs = PreferenceManager.getDefaultSharedPreferences(this);
			username = prefs.getString("username", "");
			password = prefs.getString("password", "");
			pairedDev = prefs.getString("pairedDev", "");

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	final Handler handler = new Handler();

	public void showLocation(View view) {
		application.setLocation(null);
		wsConnection.sendTextMessage("GET #LOCATION @" + pairedDev);
		final Intent intent = new Intent(this, LocationActivity.class);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Do something after DELAY
				String loc = application.getLocation();
				Bundle b = new Bundle();
				b.putString("location", loc); // Your id
				intent.putExtras(b); // Put your id to your next Intent
				startActivity(intent);
			}
		}, DELAY);
	}

	public void showPhoto(View view) {
		application.setPhoto(null);
		wsConnection.sendTextMessage("GET #PHOTO @" + pairedDev);
		final Intent intent = new Intent(this, PhotosActivity.class);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Do something after DELAY
				String photo = application.getPhoto();
				Bundle b = new Bundle();
				b.putString("photo", photo); // Your id
				intent.putExtras(b); // Put your id to your next Intent
				startActivity(intent);
			}
		}, DELAY);

	}

	public void showVideo(View view) {
		application.setVideo(null);
		wsConnection.sendTextMessage("GET #VIDEO @" + pairedDev);
		final Intent intent = new Intent(this, VideosActivity.class);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Do something after DELAY
				String vid = application.getVideo();
				Bundle b = new Bundle();
				b.putString("video", vid); // Your id
				intent.putExtras(b); // Put your id to your next Intent
				startActivity(intent);
			}
		}, DELAY);

	}

	public void showAudio(View view) {
		application.setAudio(null);
		wsConnection.sendTextMessage("GET #AUDIO @" + pairedDev);
		final Intent intent = new Intent(this, AudioActivity.class);
		// should handle waiting code here until the external device send the
		// location
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				String audio = application.getAudio();

				Bundle b = new Bundle();
				b.putString("audio", audio); // Your id
				intent.putExtras(b); // Put your id to your next Intent
				startActivity(intent);
			}
		}, DELAY);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.itemPreferences:
			startActivity(new Intent(this, PrefsActivity.class));
			// Log.d(TAG, "menuitem prefs selected");
			break;
		// start the websocket service
		case R.id.itemServiceStart:
			// startService(new Intent(this, WebSocketService.class));
			application.startService();

			break;
		case R.id.itemServiceStop:
			// stopService(new Intent(this, WebSocketService.class));
			application.stopService();

			break;
		case R.id.itemLogin:
			// Log.d(TAG, "LOGIN #name " + username + " #skey " + password
			// + " @mysensors");
			wsConnection.sendTextMessage("LOGIN #name " + username + " #skey "
					+ password + " @mysensors\n");
			// Log.d(TAG, "itemLogin selected");
			break;
		case R.id.itemShare:
			wsConnection
					.sendTextMessage("SHARE #" + "PHOTO" + " @" + pairedDev);
			wsConnection
					.sendTextMessage("SHARE #" + "AUDIO" + " @" + pairedDev);
			wsConnection
					.sendTextMessage("SHARE #" + "VIDEO" + " @" + pairedDev);
			wsConnection.sendTextMessage("SHARE #" + "LOCATION" + " @"
					+ pairedDev);

			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

}
