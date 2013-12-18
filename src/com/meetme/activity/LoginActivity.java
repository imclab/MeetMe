package com.meetme.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.meetme.R;
import com.meetme.core.HttpUtils;
import com.meetme.protocol.HttpParameters;
import com.meetme.protocol.ServerUrlStore;

public class LoginActivity extends Activity {

	private EditText loginEdit;
	private EditText passwordEdit;
	private Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginEdit = (EditText)findViewById(R.id.loginEdit);
		passwordEdit = (EditText)findViewById(R.id.passwordEdit);
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(loginListener);
	}
	
	/*
	 * Private methods
	 */
	private void login() {
		JSONObject responseJSON = null;
		String url = ServerUrlStore.LOGIN_URL;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put("email", loginEdit.getText().toString());
		parameters.put("password", passwordEdit.getText().toString());
		
		// Send request
		responseJSON = HttpUtils.post(url, parameters);
	}
	
	private OnClickListener loginListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			login();
	    }
	};
}
