package com.meetme.activity;

import static com.meetme.store.ErrorCodeStore.SUCCESS;
import static com.meetme.store.ServerParameterStore.LOGIN_EMAIL;
import static com.meetme.store.ServerParameterStore.LOGIN_PASSWORD;
import static com.meetme.store.ServerParameterStore.USER_OPERATION;
import static com.meetme.store.ServerParameterStore.USER_OPERATION_LOGIN;
import static com.meetme.store.ServerUrlStore.USER_URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.meetme.R;
import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.core.SessionManager;
import com.meetme.model.database.DatabaseHandler;
import com.meetme.model.database.Session;
import com.meetme.parser.FriendInviteNotificationParser;
import com.meetme.parser.FriendParser;
import com.meetme.parser.MeetingInviteNotificationParser;
import com.meetme.parser.MeetingParser;
import com.meetme.parser.UserParser;

public class SplashScreenActivity extends Activity {
	
	private boolean isRemembered = false;
	private Session session;
	private static int SPLASH_TIME_OUT = 2000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		
		DatabaseHandler db = new DatabaseHandler(this);
		
		session = db.getSession();
		isRemembered = (session != null);
		
		if (isRemembered) {
			new FetchData().execute();
    	} else {
    		// Wait for 2 seconds
    		new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                	Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
    	}
	}
	
	/*
	 * Load user data if logged
	 */
	private class FetchData extends AsyncTask<Void, Void, Void> {
		
		private SessionManager sessionManager = null;
		private int responseCode;
		
		private void handleLoginResponse(JSONObject responseJSON) {
			try {
				int responseCode = responseJSON.getInt("error_code");
				
				if (responseCode == SUCCESS) {
					// Instanciate parsers
					UserParser userParser = new UserParser();
					FriendParser friendParser = new FriendParser();
					MeetingParser meetingParser = new MeetingParser();
					FriendInviteNotificationParser friendNotificationParser = new FriendInviteNotificationParser();
					MeetingInviteNotificationParser meetingNotificationParser = new MeetingInviteNotificationParser();
					
					// Load data into session
					sessionManager.setUser(userParser.getFromJSON(responseJSON.getJSONObject("user")));
					sessionManager.setFriendSet(friendParser.getSetFromJSON(responseJSON, "friends"));
					sessionManager.setMeetingSet(meetingParser.getSetFromJSON(responseJSON, "meetings"));
					sessionManager.setFriendNotificationSet(friendNotificationParser.getSetFromJSON(responseJSON, "friendNotifications"));
					sessionManager.setMeetingNotificationSet(meetingNotificationParser.getSetFromJSON(responseJSON, "meetingsNotifications"));
				} 
			} catch (JSONException e) {
				Log.d(LoginActivity.class.getName(), e.getMessage(), e);
			} catch (Exception e) {
				Log.d(LoginActivity.class.getName(), e.getMessage(), e);
			}
		}
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sessionManager = SessionManager.getInstance();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
        	JSONObject responseJSON = null;
			HttpParameters parameters = new HttpParameters();
			
			// Add parameters
			parameters.put(USER_OPERATION, USER_OPERATION_LOGIN);
			parameters.put(LOGIN_EMAIL, session.getEmail());
			parameters.put(LOGIN_PASSWORD, session.getPassword());
			
			// Send request
			responseJSON = HttpUtils.post(USER_URL, parameters);
			
			// Handle response
			handleLoginResponse(responseJSON);
			
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
            if (responseCode == SUCCESS) {
            	Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
            } else {
	            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
	            startActivity(i);
            }
            
            finish();
        }
    }
}
