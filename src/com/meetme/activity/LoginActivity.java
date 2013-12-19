package com.meetme.activity;

import static com.meetme.protocol.store.ServerParameterStore.LOGIN_EMAIL;
import static com.meetme.protocol.store.ServerParameterStore.LOGIN_PASSWORD;
import static com.meetme.protocol.store.ServerUrlStore.LOGIN_URL;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.HttpUtils;
import com.meetme.protocol.HttpParameters;
import com.meetme.validator.LoginValidator;

public class LoginActivity extends Activity {

	private EditText loginEdit;
	private EditText passwordEdit;
	private Button loginButton;
	private TextView newToMeetMeTextView;
	private LoginValidator loginValidator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginEdit = (EditText)findViewById(R.id.loginEdit);
		passwordEdit = (EditText)findViewById(R.id.passwordEdit);
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(loginListener);
		newToMeetMeTextView = (TextView)findViewById(R.id.newToMeetMeLink);
		newToMeetMeTextView.setOnClickListener(newToMeetMeListener);
		loginValidator = new LoginValidator(loginEdit, passwordEdit);
	}
	
	/*
	 * Private methods
	 */
	private void login() {
		JSONObject responseJSON = null;
		String url = LOGIN_URL;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(LOGIN_EMAIL, loginEdit.getText().toString());
		parameters.put(LOGIN_PASSWORD, passwordEdit.getText().toString());
		
		// Send request
		responseJSON = HttpUtils.post(url, parameters);
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
