package com.example.myfirstapp;

import java.util.ArrayList;

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
	
	static final int GET_LOGGIN_STATUS = 1;
	static final int GET_MODE = 2;
	
	private String mCamID;
	private String mCamPass;
	private String mViewID;
	private String mViewPass;
	private String mMode;
	private String mCurrentUser;
	private String mCurrentRecepient;
	public boolean logged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		if (savedInstanceState!=null) {
			logged = savedInstanceState.getBoolean("logged");
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
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  if (logged) {
		  savedInstanceState.putBoolean("logged", true);
	}
	  
	  
//	  savedInstanceState.putDouble("myDouble", 1.9);
//	  savedInstanceState.putInt("MyInt", 1);
//	  savedInstanceState.putString("MyString", "Welcome back to Android");
	  // etc.
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  logged = savedInstanceState.getBoolean("logged");
//	  double myDouble = savedInstanceState.getDouble("myDouble");
//	  int myInt = savedInstanceState.getInt("MyInt");
//	  String myString = savedInstanceState.getString("MyString");
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
				ArrayList<String> result = data.getStringArrayListExtra("result");
				mCamID = result.get(0);
				mCamPass = result.get(1);
				mViewID = result.get(2);
				mViewPass = result.get(3);
				mMode = result.get(4);
				if (mMode.equals("CAM")) {
					mCurrentUser=mCamID;
					mCurrentRecepient=mViewID;
				} else if (mMode.equals("VIEW")){
					mCurrentUser=mViewID;
					mCurrentRecepient=mViewID;
				}
				else{
					Log.d(TAG, "error: no mode specified");
				}
				logged = true;
				Log.d(TAG, "got from activity log: " + mCamID + mCamPass+mViewID+mViewPass+mMode);
				makeConnection();
				Log.d(TAG, "Status: logged");
//				camViewSelector();
				
			}
			// Do something with the contact here (bigger example below)
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
		if (requestCode == GET_MODE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				String mode = data.getStringExtra("mode");
				Log.d(TAG, "camViewSelector returned: "+mode);
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
	/*public void camViewSelector(){
		Log.d(TAG, "camViewSelector called");
		Intent camViewSelector = new Intent(this, CamViewSelector.class);
		startActivityForResult(camViewSelector, GET_MODE);
		Log.d(TAG, "camViewSelector done");
	}*/
	public void makeConnection() {

		// server location
		final String wsuri = "ws://192.168.12.1:9000";
		try {
			mConnection.connect(wsuri, new WebSocketHandler() {
				@Override
				public void onOpen() {
					Log.d(TAG, "Status: Connected to " + wsuri);
					if (mMode.equals("CAM")){
						loginToServer(mCamID, mCamPass);
//						shareASensor("CHAT", mViewID);
					}
					else if (mMode.equals("VIEW")){
						loginToServer(mViewID, mViewPass);
						shareASensor("CHAT", mCamID);
						shareASensor("AUDIO", mCamID);
						shareASensor("VIDEO", mCamID);
						shareASensor("LOCATION", mCamID);
						
					}else{
						Log.d(TAG, "error: no mode specified");
					}
					
//					shareASensor("CHAT", "u2");
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
		String txt="@"+mCurrentRecepient+" DATA #CHAT "+message;
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
