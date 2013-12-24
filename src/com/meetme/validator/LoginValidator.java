package com.meetme.validator;

import static com.meetme.protocol.store.MessageStore.EMPTY_PASSWORD;
import static com.meetme.protocol.store.MessageStore.INVALID_EMAIL;
import android.content.Context;
import android.widget.EditText;

public class LoginValidator extends Validator {
	
	private EditText loginEdit;
	private EditText passwordEdit;
	
	/*
	 * Constructors
	 */
	public LoginValidator(Context context, EditText loginEdit, EditText passwordEdit) {
		super(context);
		this.loginEdit = loginEdit;
		this.passwordEdit = passwordEdit;
	}
	
	/*
	 * private methods
	 */
	private boolean validateLogin() {
		boolean isLoginValid = false;
		String login = loginEdit.getText().toString();
		
		// Login must be an email
		isLoginValid = android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches();
		
		if (!isLoginValid) {
			loginEdit.setError(getString(INVALID_EMAIL));
		}
		
		return isLoginValid;
	}
	
	private boolean validatePassword() {
		boolean isPasswordValid = false;
		String password = passwordEdit.getText().toString();
		
		// Password must not be empty
		isPasswordValid = !password.isEmpty();
		
		if (!isPasswordValid) {
			passwordEdit.setError(getString(EMPTY_PASSWORD));
		}
		
		return isPasswordValid;
	}
	/*
	 * Methods
	 */
	@Override
	public boolean validate() {
		return validateLogin() && validatePassword();
	}
}
