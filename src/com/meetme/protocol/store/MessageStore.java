package com.meetme.protocol.store;

public abstract class MessageStore {
	
	public static final String INVALID_EMAIL = "Email is not valid";
	public static final String INVALID_FIRSTNAME = "Firstname must contain at least 1 character";
	public static final String INVALID_LASTNAME = "Lastname must contain at least 1 character";
	public static final String INVALID_PASSWORD = "Password must contain at least 8 characters";
	
	public static final String EMPTY_PASSWORD = "Enter your password";
	public static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
	public static final String TITLE_TOO_LONG = "Title must not exceed 255 characters";
	
	public static final String WRONG_EMAIL_FULL = "The email you entered does not exist";
	public static final String WRONG_EMAIL = "Email does not exist";
	public static final String WRONG_PASSWORD_FULL = "The password you entered is wrong";
	public static final String WRONG_PASSWORD = "Wrong password";
}
