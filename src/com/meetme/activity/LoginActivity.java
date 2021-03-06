package com.meetme.activity;

import static com.meetme.store.DialogBoxesStore.LOGGING_IN;
import static com.meetme.store.DialogBoxesStore.PLEASE_WAIT;
import static com.meetme.store.ErrorCodeStore.LOGIN_WRONG_EMAIL;
import static com.meetme.store.ErrorCodeStore.LOGIN_WRONG_PASSWORD;
import static com.meetme.store.ErrorCodeStore.SUCCESS;
import static com.meetme.store.MessageStore.GENERAL_ERROR;
import static com.meetme.store.MessageStore.WRONG_EMAIL;
import static com.meetme.store.MessageStore.WRONG_EMAIL_FULL;
import static com.meetme.store.MessageStore.WRONG_PASSWORD;
import static com.meetme.store.MessageStore.WRONG_PASSWORD_FULL;
import static com.meetme.store.ServerParameterStore.LOGIN_EMAIL;
import static com.meetme.store.ServerParameterStore.LOGIN_PASSWORD;
import static com.meetme.store.ServerParameterStore.USER_OPERATION;
import static com.meetme.store.ServerParameterStore.USER_OPERATION_LOGIN;
import static com.meetme.store.ServerUrlStore.USER_URL;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.meetme.validator.LoginValidator;

public class LoginActivity extends Activity {

	private SessionManager session;
	private ScrollView scrollView;
	private TextView errorTextView;
	private EditText loginEdit;
	private EditText passwordEdit;
	private CheckBox rememberCheckBox;
	private Button loginButton;
	private TextView newToMeetMeTextView;
	private LoginValidator loginValidator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		scrollView = (ScrollView)findViewById(R.id.scrollView);
		errorTextView = (TextView)findViewById(R.id.errorText);
		loginEdit = (EditText)findViewById(R.id.loginEdit);
		passwordEdit = (EditText)findViewById(R.id.passwordEdit);
		rememberCheckBox = (CheckBox)findViewById(R.id.rememberCheckBox);
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(loginListener);
		newToMeetMeTextView = (TextView)findViewById(R.id.newToMeetMeLink);
		newToMeetMeTextView.setOnClickListener(newToMeetMeListener);
		loginValidator = new LoginValidator(getApplicationContext(), loginEdit, passwordEdit);
		
		session = SessionManager.getInstance();
		
		// Test
		loginEdit.setText("baptiste.lebail@gmail.com");
		passwordEdit.setText("12345678");
	}
	
	/*
	 * Private methods
	 */
	private void handleLoginError(int errorCode) {
		if (errorCode == LOGIN_WRONG_EMAIL) {
			this.loginEdit.setError(getString(WRONG_EMAIL));
			this.errorTextView.setText(WRONG_EMAIL_FULL);
		} else if (errorCode == LOGIN_WRONG_PASSWORD) {
			this.passwordEdit.setError(getString(WRONG_PASSWORD));
			this.errorTextView.setText(WRONG_PASSWORD_FULL);
		} else {
			// Other errors
			this.errorTextView.setText(GENERAL_ERROR);
		}
		
		scrollView.pageScroll(ScrollView.FOCUS_UP);
		scrollView.scrollTo(0, 0);
	}
	
	private void handleLoginResponse(JSONObject responseJSON) {
		try {
			final int responseCode = responseJSON.getInt("error_code");
			
			if (responseCode == SUCCESS) {
				// Instanciate parsers
				UserParser userParser = new UserParser();
				FriendParser friendParser = new FriendParser();
				MeetingParser meetingParser = new MeetingParser();
				FriendInviteNotificationParser friendNotificationParser = new FriendInviteNotificationParser();
				MeetingInviteNotificationParser meetingNotificationParser = new MeetingInviteNotificationParser();
				
				// Load data into session
				session.setUser(userParser.getFromJSON(responseJSON.getJSONObject("user")));
				session.setFriendSet(friendParser.getSetFromJSON(responseJSON, "friends"));
				session.setMeetingSet(meetingParser.getSetFromJSON(responseJSON, "meetings"));
				session.setFriendNotificationSet(friendNotificationParser.getSetFromJSON(responseJSON, "friendNotifications"));
				session.setMeetingNotificationSet(meetingNotificationParser.getSetFromJSON(responseJSON, "meetingsNotifications"));
			
				// If stay connected is checked
				if (rememberCheckBox.isChecked()) {
					DatabaseHandler db = new DatabaseHandler(LoginActivity.this);
					
					// Store session in DB 
					Session persistentSession = new Session(
							session.getUser().getEmail(), 
							passwordEdit.getText().toString()
						);
					
					if (db.getSession() == null) {
						db.addSession(persistentSession);
					} else {
						db.updateSession(persistentSession);
					}
				}
				
				// Start main activity
				Intent i = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						handleLoginError(responseCode);
					}
				});
			}
		} catch (JSONException e) {
			Log.d(LoginActivity.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.d(LoginActivity.class.getName(), e.getMessage(), e);
		}
	}
	
	private void login() {
		final ProgressDialog progressDialog = 
				ProgressDialog.show(LoginActivity.this, getString(PLEASE_WAIT), getString(LOGGING_IN), true);
		progressDialog.setCancelable(true);
		
		new Thread(new Runnable() {
			JSONObject responseJSON = null;
			HttpParameters parameters = new HttpParameters();
			
			@Override
			public void run() {
				// Add parameters
				parameters.put(USER_OPERATION, USER_OPERATION_LOGIN);
				parameters.put(LOGIN_EMAIL, loginEdit.getText().toString());
				parameters.put(LOGIN_PASSWORD, passwordEdit.getText().toString());
				
				// Send request
				responseJSON = HttpUtils.post(USER_URL, parameters);
			
				// Handle response
				handleLoginResponse(responseJSON);
				
				progressDialog.dismiss();
			}
		}).start();
	}
	
	/*
	 * Listeners
	 */
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
