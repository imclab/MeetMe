package com.meetme.validator;

import android.widget.EditText;
import static com.meetme.protocol.store.ValidatorStore.*;

public class LoginValidator {
	
	private EditText loginEdit;
	private EditText passwordEdit;
	
	/*
	 * Constructors
	 */
	public LoginValidator(EditText loginEdit, EditText passwordEdit) {
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
			loginEdit.setError(INVALID_EMAIL);
		}
		
		return isLoginValid;
	}
	
	private boolean validatePassword() {
		boolean isPasswordValid = false;
		String password = passwordEdit.getText().toString();
		int passwordLength = password.length();
		
		// Password must be at least 8 characters
		isPasswordValid = (passwordLength >= 8 && passwordLength <= 80);
		
		if (!isPasswordValid) {
			passwordEdit.setError(INVALID_PASSWORD);
		}
		
		return isPasswordValid;
	}
	
	/*
	 * Methods
	 */
	public boolean validate() {
		return (validateLogin() && validatePassword());
	}
}
