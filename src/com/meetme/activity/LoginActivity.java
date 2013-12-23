package com.meetme.activity;

import static com.meetme.protocol.store.ErrorCodeStore.LOGIN_WRONG_EMAIL;
import static com.meetme.protocol.store.ErrorCodeStore.LOGIN_WRONG_PASSWORD;
import static com.meetme.protocol.store.ErrorCodeStore.SUCCESS;
import static com.meetme.protocol.store.MessageStore.WRONG_EMAIL;
import static com.meetme.protocol.store.MessageStore.WRONG_EMAIL_FULL;
import static com.meetme.protocol.store.MessageStore.WRONG_PASSWORD;
import static com.meetme.protocol.store.MessageStore.WRONG_PASSWORD_FULL;
import static com.meetme.protocol.store.ServerParameterStore.LOGIN_EMAIL;
import static com.meetme.protocol.store.ServerParameterStore.LOGIN_PASSWORD;
import static com.meetme.protocol.store.ServerUrlStore.LOGIN_URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.HttpUtils;
import com.meetme.core.SessionManager;
import com.meetme.protocol.HttpParameters;
import com.meetme.validator.LoginValidator;

public class LoginActivity extends Activity {

	private SessionManager session;
	private TextView errorTextView;
	private EditText loginEdit;
	private EditText passwordEdit;
	private Button loginButton;
	private TextView newToMeetMeTextView;
	private LoginValidator loginValidator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		errorTextView = (TextView)findViewById(R.id.errorText);
		loginEdit = (EditText)findViewById(R.id.loginEdit);
		passwordEdit = (EditText)findViewById(R.id.passwordEdit);
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(loginListener);
		newToMeetMeTextView = (TextView)findViewById(R.id.newToMeetMeLink);
		newToMeetMeTextView.setOnClickListener(newToMeetMeListener);
		loginValidator = new LoginValidator(loginEdit, passwordEdit);
		
		session = SessionManager.getInstance();
	}
	
	/*
	 * Private methods
	 */
	private void handleLoginError(int errorCode) {
		if (errorCode == LOGIN_WRONG_EMAIL) {
			this.loginEdit.setError(WRONG_EMAIL);
			this.errorTextView.setText(WRONG_EMAIL_FULL);
		} else if (errorCode == LOGIN_WRONG_PASSWORD) {
			this.passwordEdit.setError(WRONG_PASSWORD);
			this.errorTextView.setText(WRONG_PASSWORD_FULL);
		} else {
			// Other errors
		}
	}
	
	private void handleLoginResponse(JSONObject responseJSON) {
		try {
			final int responseCode = responseJSON.getInt("error_code");
			
			if (responseCode == SUCCESS) {
				final String userToken = responseJSON.getString("token");
				
						session.setUserToken(userToken);
						session.updateFriendSet();
						session.updateMeetingSet();
			
				// Start main activity
				Intent i = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(i);
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						handleLoginError(responseCode);
					}
				});
			}
		} catch (JSONException e) {
			Log.d(LoginActivity.class.getName(), e.getMessage());
		} catch (Exception e) {
			Log.d(LoginActivity.class.getName(), e.getMessage());
		}
	}
	
	private void login() {
		final ProgressDialog progressDialog = 
				ProgressDialog.show(LoginActivity.this, "Please wait...", "Logging in...", true);
		progressDialog.setCancelable(true);
		
		new Thread(new Runnable() {
			JSONObject responseJSON = null;
			String url = LOGIN_URL;
			HttpParameters parameters = new HttpParameters();
			
			@Override
			public void run() {
				// Add parameters
				parameters.put(LOGIN_EMAIL, loginEdit.getText().toString());
				parameters.put(LOGIN_PASSWORD, passwordEdit.getText().toString());
				
				// Send request
				responseJSON = HttpUtils.post(url, parameters);
			
				// Handle response
				handleLoginResponse(responseJSON);
				
				progressDialog.dismiss();
			}
		}).start();
	}
	
	private OnClickListener loginListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (loginValidator.validate()) {
				login();
			}
	    }
	};
	
	private OnClickListener newToMeetMeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Start registration activity
			Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
			startActivity(intent);
	    }
	};
}
