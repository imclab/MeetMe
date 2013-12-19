package com.meetme.protocol.store;

public abstract class ValidatorStore {
	
	public static final String INVALID_EMAIL = "Email is not valid";
	public static final String INVALID_FIRSTNAME = "Firstname must contain at least 1 character";
	public static final String INVALID_LASTNAME = "Lastname must contain at least 1 character";
	public static final String INVALID_PASSWORD = "Password must contain at least 8 characters";
	public static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
}
