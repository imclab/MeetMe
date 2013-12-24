package com.meetme.validator;

import static com.meetme.protocol.store.MessageStore.INVALID_EMAIL;
import static com.meetme.protocol.store.MessageStore.INVALID_FIRSTNAME;
import static com.meetme.protocol.store.MessageStore.INVALID_LASTNAME;
import static com.meetme.protocol.store.MessageStore.INVALID_PASSWORD;
import static com.meetme.protocol.store.MessageStore.PASSWORDS_DO_NOT_MATCH;
import android.content.Context;
import android.widget.EditText;


public class RegistrationValidator extends Validator {
	
	private EditText emailEdit;
	private EditText firstnameEdit;
	private EditText lastnameEdit;
	private EditText passwordEdit;
	private EditText repeatPasswordEdit;
	
	/*
	 * Constructors
	 */
	public RegistrationValidator(
			Context context,
			EditText emailEdit, 
			EditText firstnameEdit,
			EditText lastnameEdit, 
			EditText passwordEdit,
			EditText repeatPasswordEdit) {
		super(context);
		this.emailEdit = emailEdit;
		this.firstnameEdit = firstnameEdit;
		this.lastnameEdit = lastnameEdit;
		this.passwordEdit = passwordEdit;
		this.repeatPasswordEdit = repeatPasswordEdit;
	}
	
	/*
	 * private methods
	 */
	private boolean validateEmail() {
		boolean isEmailValid = false;
		String email = emailEdit.getText().toString();
		
		isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		
		if (!isEmailValid) {
			emailEdit.setError(getString(INVALID_EMAIL));
		}
		
		return isEmailValid;
	}
	
	private boolean validateFirstname() {
		boolean isFirstnameValid = false;
		String firstname = firstnameEdit.getText().toString();
		int firstnameLength = firstname.length();
		
		// Firstname must contain at least 1 character
		isFirstnameValid = (firstnameLength >= 1 && firstnameLength <=90);
		
		if (!isFirstnameValid) {
			firstnameEdit.setError(getString(INVALID_FIRSTNAME));
		}
		
		return isFirstnameValid;
	}
	
	private boolean validateLastname() {
		boolean isLastnameValid = false;
		
		String lastname = lastnameEdit.getText().toString();
		int lastnameLength = lastname.length();
		
		// Lastname must contain at least 1 character
		isLastnameValid = (lastnameLength >= 1 && lastnameLength <=90);
		
		if (!isLastnameValid) {
			lastnameEdit.setError(getString(INVALID_LASTNAME));
		}
		
		return isLastnameValid;
	}
	
	private boolean validatePassword() {
		boolean isPasswordValid = false;
		String password = passwordEdit.getText().toString();
		int passwordLength = password.length();
		
		// Password must be at least 8 characters
		isPasswordValid = (passwordLength >= 8 && passwordLength <= 80);
		
		if (!isPasswordValid) {
			passwordEdit.setError(getString(INVALID_PASSWORD));
		}
		
		return isPasswordValid;
	}
	
	private boolean validateRepeatPassword() {
		boolean isRepeatPasswordValid = false;
		String repeatPassword = repeatPasswordEdit.getText().toString();
		String password = passwordEdit.getText().toString();
		
		// Passwords must be equal
		isRepeatPasswordValid = repeatPassword.equals(password);
		
		if (!isRepeatPasswordValid) {
			repeatPasswordEdit.setError(getString(PASSWORDS_DO_NOT_MATCH));
		}
		
		return isRepeatPasswordValid;
	}
	
	/*
	 * Methods
	 */
	public boolean validate() {
		return (validateEmail()
				&& validateFirstname()
				&& validateLastname()
				&& validatePassword()
				&& validateRepeatPassword()
			);
	}
}
