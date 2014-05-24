package com.example.myfirstapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "com.example.myfirstapp";
	private final WebSocketConnection mConnection = new WebSocketConnection();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		makeConnection();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
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

	/** Called when the user clicks the Send button */
	public void makeConnection() {

		// EditText editText = (EditText) findViewById(R.id.edit_message);
		String userName = "u1";
		String cipherpass = "h81smMHgTTai9zw1XIqcArZ8ipEe0VuUXZqaMQAfQh2IkC4Qmsm+kaElbh6fYgwSFMGQGoLmMjf8M5beZOm8+Y7dAayGUcWNxrF2XCGK7pL4XUoynboWCh09DPf1MjV/ZY9pLWCi8PDeLRvdGYV6VJBLwuELfBCCaSNX9oeeOX4=";
		final String message = "LOGIN #name " + userName + " #enckey "
				+ cipherpass + " @mysensors\n";
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

	public void sendMessage(View view) {

		EditText editText = (EditText) findViewById(R.id.edit_message);
		final String message = editText.getText().toString();
		// final String wsuri = "ws://192.168.12.1:9000";
		try {

			mConnection.sendTextMessage(message);
			editText.setText("");
		} catch (Exception e) {

			Log.d(TAG, e.toString());
		}
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	public void parseMessage(String message) {
		Log.d(TAG, "Server said: " + message);
		EditText history = (EditText)findViewById(R.id.history);

		//in your OnCreate() method
		history.append("\n...\n"+message);
		//String[] messageParts=message.split("\\s+");
	}

}
