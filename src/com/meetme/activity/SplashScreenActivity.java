package com.meetme.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.meetme.R;
import com.meetme.core.SessionManager;

public class SplashScreenActivity extends Activity {
	
	private boolean isRemembered = false;
	private String userToken;
	private static int SPLASH_TIME_OUT = 2000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		
		/*
         * check if there is a user token in database
         * if (tokenInBase) -> isRemembered = true; 
         * else -> isRemembered = false 
         */
		
		if (isRemembered) {
			new fetchData().execute(userToken);
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
	private class fetchData extends AsyncTask<String, Void, Void> {
		
		private SessionManager session = null;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            session = SessionManager.getInstance();
        }
 
        @Override
        protected Void doInBackground(String... userToken) {
    		session.setUserToken(userToken[0]);
    		/*
    		 * Download friend & meeting list of the user
    		 */
    		session.updateFriendSet();
    		session.updateMeetingSet();
        	
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
