package com.example.myguard1;

import java.util.HashMap;

import android.util.Log;

public class QueryHandler {

	private static final String TAG = QueryParser.class.getName();

	/**
	 * Generate login query and send to server
	 * 
	 * @param application
	 *            application object instance
	 */
	// try to use parameters as hash map to as string(current)
	public static void handleLogin(MyGuardApplication application) {
		// generate login query with user credentials
		// sample query - LOGIN #name era #skey 123 @mysensors
		String command = "LOGIN";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", application.getUsername());
		params.put("skey", application.getPassword());
		String message = QueryParser.getMessage(new Query(command, "mysensors",
				params));

//		System.out.println("login message " + message);
		application.getWsConnection().sendTextMessage(message);
	}

	/**
	 * Handle query message from web socket
	 * 
	 * @param application
	 *            application object
	 * @param payload
	 *            payload from server
	 */
	public static void handleQuery(MyGuardApplication application,
			String payload) {
//		System.out.println(payload);
		try {
			// need to parse query in order to further processing
			Query query = QueryParser.parse(payload);

			if (query.getCommand().equalsIgnoreCase("STATUS")) {
				// STATUS query
				// handle LOGIN and SHARE status from handleStatus
				// handleStatusQuery(application, query);
				Log.d(TAG, "STATUS :" + payload);
			} else if (query.getCommand().equalsIgnoreCase("SHARE")) {
				// SHARE query
				// handle SHARE query from handleShare
				// handleShareQuery(application, query);
				Log.d(TAG, "SHARE :" + payload);
			} else if (query.getCommand().equalsIgnoreCase("GET")) {
				// GET query
				// handle via handleGet
				handleGetQuery(application, query);
				Log.d(TAG, "GET :" + payload);
			} else if (query.getCommand().equalsIgnoreCase("DATA")) {
				// DATA query
				// handle via handleData
				handleDataQuery(application, query);
				Log.d(TAG, "DATA :" + payload);
			} else {
				// invalid query or not supporting query
				// System.out.println("INVALID/UN-SUPPORTING query");
				Log.d(TAG, "INVALID/UN-SUPPORTING query :" + payload);
			}
		} catch (InvalidQueryException e) {
			System.out.println(e);
		}
	}

	private static void handleDataQuery(MyGuardApplication application,
			Query query) {
		/*
		 * Log.d(TAG, "DATA recieved :"+query.toString()); Log.d(TAG,
		 * query.getParams().toString()); Log.d(TAG,
		 * query.getUser().toString());
		 */
		System.out.println("data query recieved :"+query.toString());
		String params = query.getParams().toString().split("=")[0];

		if (params.equalsIgnoreCase("{audio")) {
			Log.d(TAG, "audio data recieved");
			handleDataAudio(application, query);
		} else if (params.equalsIgnoreCase("{video")) {
			Log.d(TAG, "video data recieved");
			handleDataVideo(application, query);
		} else if (params.equalsIgnoreCase("{location")) {
			Log.d(TAG, "location data recieved");
			handleDataLocation(application, query);
		} else if (params.equalsIgnoreCase("{photo")) {
			Log.d(TAG, "photo data recieved");
			handleDataPhoto(application, query);
		} else {
			Log.d(TAG, "unknown parameters :" + params);
		}
	}
	//what to do when video link received
	private static void handleDataVideo(MyGuardApplication application,
			Query query) {
		if (query.getParams().get("VIDEO").equals(null)) {
			Log.e(TAG, "null recieved as vid link");
			application.setVideo("dummy");
		} else {
			application.setVideo(query.getParams().get("VIDEO"));
		}
		
	}
	//what to do when audio link received
	private static void handleDataAudio(MyGuardApplication application,
			Query query) {
		application.setAudio(query.getParams().get("AUDIO"));
	}
	//what to do when photo link received
	private static void handleDataPhoto(MyGuardApplication application,
			Query query) {
		application.setPhoto(query.getParams().get("PHOTO"));
	}
	//what to do when location string received	
	private static void handleDataLocation(MyGuardApplication application,
			Query query) {
//		System.out.println("query para :" + query.getParams());
		//set the location variable on viewer's app so it could be used with the location activity
		application.setLocation(query.getParams().get("LOCATION"));
//		System.out.println("location :"+query.getParams().get("LOCATION"));
		
	}

	private static <params> void handleGetQuery(MyGuardApplication application,
			Query query) {
		Log.d(TAG, "GET request recieved :" + query.toString());
//		Log.d(TAG, "GET params: " + query.getParams());
		String params = query.getParams().toString();
		if (params.equalsIgnoreCase("{audio=audio}")) {
			Log.d(TAG, "audio request recieved");
			handleGetAudio(application, query);
		} else if (params.equalsIgnoreCase("{video=video}")) {
			Log.d(TAG, "video request recieved");
			handleGetVideo(application, query);
		} else if (params.equalsIgnoreCase("{location=location}")) {
			Log.d(TAG, "location request recieved");
			handleGetLocation(application, query);
		}else if (params.equalsIgnoreCase("{photo=photo}")) {
			Log.d(TAG, "photo request recieved");
			handleGetPhoto(application, query);
		}
	}
	
	
	private static void handleGetPhoto(MyGuardApplication application,
			Query query) {
		String user = query.getUser();
		// get the location string add puutu's code here
		String photo = "photo_link_as_req_from::"+user;
		
		// fix the LOCATION message
		String message = "DATA #PHOTO " + photo + " @" + user;
//		System.out.println("message :" + message);
		application.getWsConnection().sendTextMessage(message);
		
		        
	}
	/**
	 * handle get video requests
	 * 
	 * @param application
	 * @param query
	 */
	private static void handleGetVideo(MyGuardApplication application,
			Query query) {
		String user = query.getUser();
		// get the location string add namil's code here
		String vid = "vid_link_as_req_from::"+user;
		
		// fix the LOCATION message
		String message = "DATA #VIDEO " + vid + " @" + user;
//		System.out.println("message :" + message);
		application.getWsConnection().sendTextMessage(message);

	}

	/**
	 * handle get Audio requests
	 * 
	 * @param application
	 * @param query
	 */
	private static void handleGetAudio(MyGuardApplication application,
			Query query) {
		String user = query.getUser();
		// get the location string add namil's code here
		String aud = "audio_link_as_req_from::"+user;
		
		// fix the LOCATION message
		String message = "DATA #AUDIO " + aud + " @" + user;
//		System.out.println("message :" + message);
		application.getWsConnection().sendTextMessage(message);

	}

	/**
	 * handle get Location requests
	 * 
	 * @param application
	 * @param query
	 */
	private static void handleGetLocation(MyGuardApplication application,
			Query query) {
		String user = query.getUser();
		// get the location string add puutu's code here
		String location = "loc_str_as_req_from::"+user;
		
		// fix the LOCATION message
		String message = "DATA #LOCATION " + location + " @" + user;
//		System.out.println("message :" + message);
		application.getWsConnection().sendTextMessage(message);

	}
	//this won't need(probably)
	public static void handleQuery(MyGuardApplication application,
			byte[] payload) {
		try {
			Log.d(TAG, "DATA :" + "binary payload recieving");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Handle STATUS query from server
	 * 
	 * @param application
	 *            application
	 * @param query
	 *            parsed query
	 */
	/*
	 * private static void handleStatusQuery(MyGuardApplication application,
	 * Query query) { // get status from query String status = "success";
	 * 
	 * if (query.getParams().containsKey("login")) { // login status status =
	 * query.getParams().get("login"); } else if
	 * (query.getParams().containsKey("share")){ // share status status =
	 * query.getParams().get("share"); }
	 * 
	 * // just send status to available handler sendMessage(application,
	 * status); }
	 */

	/**
	 * Handle SHARE query from server
	 * 
	 * @param application
	 *            application
	 * @param query
	 *            parsed query
	 */
	/*
	 * private static void handleShareQuery(MyGuardApplication application,
	 * Query query) { // add new sensor to friend sensor list that shared in
	 * application Sensor sensor = new Sensor(query.getUser(), "Location",
	 * "Location", false, false);
	 * if(!application.getFiendSensorList().contains(sensor))
	 * application.getFiendSensorList().add(sensor);
	 * 
	 * // currently we have to launch friend sensor // update notification to
	 * notify user about incoming query/ share request
	 * application.setSensorType(MyGuardApplication.FRIENDS_SENSORS);
	 * NotificationUtils.updateNotification(application.getApplicationContext(),
	 * "Location @" + query.getUser()); }
	 */

	/**
	 * Handle GET query from server
	 * 
	 * @param application
	 *            application
	 * @param query
	 *            parsed query
	 */
	/*
	 * private static void handleGetQuery(MyGuardApplication application, Query
	 * query) { // get location by starting location service
	 * if(application.getWsConnection().isConnected()) { // current location
	 * request is from web socket service // start location service
	 * application.setRequestFromFriend(true);
	 * application.setRequestQuery(query); Intent serviceIntent = new
	 * Intent(application.getApplicationContext(), GpsReadingService.class);
	 * application.getApplicationContext().startService(serviceIntent); } }
	 */

	/**
	 * Handle DATA query from server
	 * 
	 * @param application
	 *            application
	 * @param query
	 *            parsed query
	 */
	/*
	 * private static void handleDataQuery(MyGuardApplication application, Query
	 * query) { if(query.getUser().equalsIgnoreCase("mysensors")) { // this is a
	 * status query // @mysensors DATA #msg LoginSuccess // just send status to
	 * available handler String status = query.getParams().get("msg");
	 * sendMessage(application, status); } else { // from a specific user //
	 * create LatLon object from query params // we assume incoming query
	 * contains lat lon values LatLon latLon = new
	 * LatLon(query.getParams().get("lat"), query.getParams().get("lon"));
	 * application.setDataQuery(query);
	 * 
	 * // send message to available handler to notify incoming sensor value
	 * sendMessage(application, latLon); } }
	 */

	/**
	 * Send message to appropriate handler
	 * 
	 * @param application
	 *            application
	 * @param obj
	 *            payload from server
	 */
	/*
	 * private static void sendMessage(MyGuardApplication application, Object
	 * obj) { Message message = Message.obtain(); message.obj = obj; if
	 * (application.getHandler()!=null) {
	 * application.getHandler().sendMessage(message); } }
	 */

}
