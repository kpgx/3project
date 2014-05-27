package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "com.example.myfirstapp";
	private final WebSocketConnection mConnection = new WebSocketConnection();
	public boolean logged = false;
	static final int GET_LOGGIN_STATUS = 1;
	static String mUserName;
	static String mPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		if (!logged) {
			Log.d(TAG, "not logged, trying to log");
			login();
			Log.d(TAG, "logging completed");
		}

	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mConnection.isConnected()) {
			mConnection.disconnect();
		}

	}

	public void login() {
		Log.d(TAG, "login method called");
		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, GET_LOGGIN_STATUS);
		Log.d(TAG, "login method done");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		Log.d(TAG, "got result");
		if (requestCode == GET_LOGGIN_STATUS) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				// The user picked a contact.
				// The Intent's data Uri identifies which contact was selected.
				String[] result = data.getStringExtra("result").split(":");
				mUserName = result[0];
				mPassword = result[1];
				logged = true;
				Log.d(TAG, "CONNECTING" + mUserName + mPassword);
				makeConnection(mUserName, mPassword);
				Log.d(TAG, "Status: logged");
			}
			// Do something with the contact here (bigger example below)
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}

	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public void makeConnection(String userName, String passwd) {

		// server location
		final String wsuri = "ws://192.168.12.1:9000";
		try {
			mConnection.connect(wsuri, new WebSocketHandler() {
				@Override
				public void onOpen() {
					Log.d(TAG, "Status: Connected to " + wsuri);
					loginToServer(mUserName, mPassword);
					shareASensor("CHAT", "u2");
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

	public void loginToServer(String userName, String passwd) {
		final String message = "LOGIN #name " + userName + " #skey " + passwd
				+ " @mysensors\n";
		mConnection.sendTextMessage(message);
	}

	public void sendMessage(View view) {

		EditText editText = (EditText) findViewById(R.id.edit_message);
		final String message = editText.getText().toString();
		String txt="@"+mUserName+" DATA #CHAT "+message;
		try {

			mConnection.sendTextMessage(txt);
			editText.setText("");
		} catch (Exception e) {

			Log.d(TAG, e.toString());
		}
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	public void parseMessage(String message) {
		Log.d(TAG, "Server said: " + message);
		String tokens[] = message.split(" ");
		int noOfTokens = tokens.length;

		if (noOfTokens > 3) {
			if (tokens[2].equals("#CHAT")) {
				String txts[] = message.split(" ", 4);
				handleIncommingChatting(txts[0], txts[3]);

			}
		}

	}

	public void handleIncommingChatting(String fromWho, String message) {
		EditText history = (EditText) findViewById(R.id.history);
		history.append("\n...\n" + fromWho + ": " + message);

	}

	public void shareASensor(String sensor, String whoWith) {
		mConnection.sendTextMessage("SHARE #" + sensor + " @" + whoWith);
	}

}
