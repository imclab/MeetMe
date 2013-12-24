package com.meetme.activity;

import static com.meetme.protocol.store.ServerParameterStore.REGISTRATION_EMAIL;
import static com.meetme.protocol.store.ServerParameterStore.REGISTRATION_FIRSTNAME;
import static com.meetme.protocol.store.ServerParameterStore.REGISTRATION_LASTNAME;
import static com.meetme.protocol.store.ServerParameterStore.REGISTRATION_PASSWORD;
import static com.meetme.protocol.store.ServerUrlStore.REGISTRATION_URL;

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
import com.meetme.validator.RegistrationValidator;

public class RegistrationActivity extends Activity {

	private EditText emailEdit;
	private EditText firstnameEdit;
	private EditText lastnameEdit;
	private EditText passwordEdit;
	private EditText repeatPasswordEdit;
	private Button registrationButton;
	private TextView alreadyHaveAnAccountTextView;
	private RegistrationValidator registrationValidator;
	
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
		alreadyHaveAnAccountTextView = (TextView)findViewById(R.id.alreadyHaveAnAccountLink);
		alreadyHaveAnAccountTextView.setOnClickListener(alreadyHaveAnAccountListener);
		
		registrationValidator = new RegistrationValidator(
				emailEdit, 
				firstnameEdit, 
				lastnameEdit, 
				passwordEdit, 
				repeatPasswordEdit);
	}
	
	/*
	 * Private methods
	 */
	private void register() {
		JSONObject responseJSON = null;
		String url = REGISTRATION_URL;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(REGISTRATION_EMAIL, emailEdit.getText().toString());
		parameters.put(REGISTRATION_FIRSTNAME, firstnameEdit.getText().toString());
		parameters.put(REGISTRATION_LASTNAME, lastnameEdit.getText().toString());
		parameters.put(REGISTRATION_PASSWORD, passwordEdit.getText().toString());
		
		// Send request
		responseJSON = HttpUtils.post(url, parameters);
	}
	
	private OnClickListener registerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (registrationValidator.validate()) {
				register();
			}
	    }
	};
	
	private OnClickListener alreadyHaveAnAccountListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Start Log In activity
			Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
			startActivity(intent);
		}
	};
}
