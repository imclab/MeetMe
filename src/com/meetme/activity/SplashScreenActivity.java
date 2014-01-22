package com.meetme.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.User;

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
			new FetchData().execute(userToken);
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
	private class FetchData extends AsyncTask<String, Void, Void> {
		
		private SessionManager session = null;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            session = SessionManager.getInstance();
        }
 
        @Override
        protected Void doInBackground(String... userToken) {
    		// Init session
        	User user = new User();
    		user.setId(1);
    		user.setEmail("email");
    		user.setFirstname("firstname");
    		user.setLastname("lastname");
    		user.setToken(SplashScreenActivity.this.userToken);
        	session.setUser(user);
        	
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent i = new Intent(SplashScreenActivity.this, MeetingsActivity.class);
            startActivity(i);
            finish();
        }
    }
}
