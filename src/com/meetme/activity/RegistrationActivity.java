package com.meetme.activity;

import static com.meetme.protocol.store.DialogBoxesStore.PLEASE_WAIT;
import static com.meetme.protocol.store.DialogBoxesStore.REGISTRATION;
import static com.meetme.protocol.store.DialogBoxesStore.REGISTRATION_SUCCESS_TITLE;
import static com.meetme.protocol.store.DialogBoxesStore.REGISTRATION_SUCCESS_MESSAGE;
import static com.meetme.protocol.store.DialogBoxesStore.VALIDATED_BUTTON;
import static com.meetme.protocol.store.ErrorCodeStore.REGISTRATION_EMAIL_ALREADY_EXIST;
import static com.meetme.protocol.store.ErrorCodeStore.SUCCESS;
import static com.meetme.protocol.store.MessageStore.EMAIL_ALREADY_EXISTS;
import static com.meetme.protocol.store.MessageStore.GENERAL_ERROR;
import static com.meetme.protocol.store.ServerParameterStore.REGISTRATION_EMAIL;
import static com.meetme.protocol.store.ServerParameterStore.REGISTRATION_FIRSTNAME;
import static com.meetme.protocol.store.ServerParameterStore.REGISTRATION_LASTNAME;
import static com.meetme.protocol.store.ServerParameterStore.REGISTRATION_PASSWORD;
import static com.meetme.protocol.store.ServerUrlStore.REGISTRATION_URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.HttpUtils;
import com.meetme.protocol.HttpParameters;
import com.meetme.validator.RegistrationValidator;

public class RegistrationActivity extends Activity {

	private ScrollView scrollView;
	private TextView errorTextView;
	private EditText emailEdit;
	private EditText firstnameEdit;
	private EditText lastnameEdit;
	private EditText passwordEdit;
	private EditText repeatPasswordEdit;
	private Button registrationButton;
	private TextView alreadyHaveAnAccountTextView;
	private RegistrationValidator registrationValidator;
	
	// To keep track of the registration process
	private static int registrationState = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		scrollView = (ScrollView)findViewById(R.id.scrollView);
		errorTextView = (TextView)findViewById(R.id.errorText);
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
				getApplicationContext(),
				emailEdit, 
				firstnameEdit, 
				lastnameEdit, 
				passwordEdit, 
				repeatPasswordEdit
			);
	}
	
	/*
	 * Private methods
	 */
	private void handleRegistrationError(int errorCode) {
		if (errorCode == REGISTRATION_EMAIL_ALREADY_EXIST) {
			this.emailEdit.setError(getString(EMAIL_ALREADY_EXISTS));
			this.errorTextView.setText(EMAIL_ALREADY_EXISTS);
		} else {
			// Other errors
			this.errorTextView.setText(GENERAL_ERROR);
		}
		
		scrollView.pageScroll(ScrollView.FOCUS_UP);
		scrollView.scrollTo(0, 0);
	}
	
	private void handleRegistrationResponse(JSONObject responseJSON) {
		try {
			final int responseCode = responseJSON.getInt("error_code");
			
			if (responseCode == SUCCESS) {
				registrationState = SUCCESS;
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
							handleRegistrationError(responseCode);
						};
					});
			}
		} catch (JSONException e) {
			Log.d(LoginActivity.class.getName(), e.getMessage());
		} catch (Exception e) {
			Log.d(LoginActivity.class.getName(), e.getMessage());
		}
	}
	
	private void register() {
		new RegistrationProcess().execute();
	}
	
	/*
	 * Registration task
	 */
	private class RegistrationProcess extends AsyncTask<Void, Void, Void> {
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		ProgressDialog progressDialog;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registrationState = -1;
            progressDialog = ProgressDialog.show(RegistrationActivity.this, getString(PLEASE_WAIT), getString(REGISTRATION), true);
			progressDialog.setCancelable(true);
        }
 
        @Override
        protected Void doInBackground(Void...voids) {
        	// Add parameters
			parameters.put(REGISTRATION_EMAIL, emailEdit.getText().toString());
			parameters.put(REGISTRATION_FIRSTNAME, firstnameEdit.getText().toString());
			parameters.put(REGISTRATION_LASTNAME, lastnameEdit.getText().toString());
			parameters.put(REGISTRATION_PASSWORD, passwordEdit.getText().toString());
			
			// Send request
			responseJSON = HttpUtils.post(REGISTRATION_URL, parameters);
			
			// Handle response
			handleRegistrationResponse(responseJSON);
			
			progressDialog.dismiss();
			
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
    		if (registrationState == SUCCESS) {
    			AlertDialog registrationSuccessInfoDialog = 
    					new AlertDialog.Builder(RegistrationActivity.this).create();
     
    			registrationSuccessInfoDialog.setTitle(getString(REGISTRATION_SUCCESS_TITLE));
    			registrationSuccessInfoDialog.setMessage(getString(REGISTRATION_SUCCESS_MESSAGE));
    			registrationSuccessInfoDialog.setIcon(R.drawable.validated_icon);
    			registrationSuccessInfoDialog.setButton(
    					AlertDialog.BUTTON_POSITIVE, 
    					getString(VALIDATED_BUTTON),
    					registationSuccessInfoDialogButtonListener
    				);
    			registrationSuccessInfoDialog.show();
    		}
        }
    }
	
	/*
	 * Listeners
	 */
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
	
	private DialogInterface.OnClickListener registationSuccessInfoDialogButtonListener = 
		new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int which) {
        		Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
				startActivity(i);
				finish();
        	}
		};
}
