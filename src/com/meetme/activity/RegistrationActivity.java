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

public class RegistrationActivity extends Activity {

	private EditText emailEdit;
	private EditText firstnameEdit;
	private EditText lastnameEdit;
	private EditText passwordEdit;
	private EditText repeatPasswordEdit;
	private Button registrationButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		emailEdit = (EditText)findViewById(R.id.emailEdit);
		firstnameEdit = (EditText)findViewById(R.id.firstnameEdit);
		lastnameEdit = (EditText)findViewById(R.id.lastnameEdit);
		passwordEdit = (EditText)findViewById(R.id.passwordEdit);
		repeatPasswordEdit = (EditText)findViewById(R.id.repeatPasswordEdit);
		registrationButton = (Button)findViewById(R.id.registrationButton);
		registrationButton.setOnClickListener(registerListener);
	}
	
	/*
	 * Private methods
	 */
	private void register() {
		JSONObject responseJSON = null;
		String url = ServerUrlStore.REGISTRATION_URL;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put("email", emailEdit.getText().toString());
		parameters.put("firstname", firstnameEdit.getText().toString());
		parameters.put("lastname", lastnameEdit.getText().toString());
		parameters.put("password", passwordEdit.getText().toString());
		
		// Send request
		responseJSON = HttpUtils.post(url, parameters);
	}
	
	private OnClickListener registerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			register();
	    }
	};
}
