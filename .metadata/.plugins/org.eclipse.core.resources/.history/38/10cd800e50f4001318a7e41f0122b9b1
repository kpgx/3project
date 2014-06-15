package com.example.myguard1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.tavendo.autobahn.WebSocketConnection;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	WebSocketConnection wsConnection;
	// preferences
	SharedPreferences prefs;
	private String username;
	private String password;
	private String pairedDev;
	// UI references
	private EditText messageView;
	private EditText historyView;
	private Button sendButton;
	// Data references
	private String message;
	private String history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		messageView = (EditText) findViewById(R.id.editTextMessage);
		historyView = (EditText) findViewById(R.id.editTextHistory);
		sendButton = (Button) findViewById(R.id.buttonSend);
		wsConnection = ((MyGuardApplication) getApplication())
				.getWsConnection();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		username = prefs.getString("username", "");
		password = prefs.getString("password", "");
		pairedDev = prefs.getString("pairedDev", "");
		history = historyView.toString();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.itemPreferences:
			startActivity(new Intent(this, PrefsActivity.class));
			Log.d(TAG, "menuitem prefs selected");
			break;
		case R.id.itemServiceStart:
			startService(new Intent(this, WebSocketService.class));
			Log.d(TAG, "menuitem start selected");
			break;
		case R.id.itemServiceStop:
			stopService(new Intent(this, WebSocketService.class));
			Log.d(TAG, "menuitem stop selected");
			break;
		case R.id.itemLogin:
			wsConnection.sendTextMessage("LOGIN #name " + username + " #skey "
					+ password + " @mysensors\n");
			Log.d(TAG, "itemLogin selected");
			break;
		case R.id.itemShare:
			wsConnection.sendTextMessage("SHARE #" + "CHAT" + " @" + pairedDev);
			Log.d(TAG, "itemShare selected");
//			code to check location retrieve
			/*try {
				String location = getLocation();
				Log.d(TAG, location);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d(TAG, e.toString());
			}*/
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

	public void sendMessage(View view) {
		message = messageView.getText().toString();
		wsConnection.sendTextMessage(message);
	}
//	yaka's location retrieve code.doesn't work returns null
	/*public String getLocation() {
		String coordinates;
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locManager.getBestProvider(criteria, false);
		Location loc = locManager.getLastKnownLocation(provider);
		String lat = String.valueOf(loc.getLatitude());
		String lng = String.valueOf(loc.getLongitude());
		coordinates = lat + "/" + lng;
		return coordinates;
	}*/

}
