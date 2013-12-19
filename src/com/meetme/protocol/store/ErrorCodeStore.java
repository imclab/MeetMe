package com.meetme.protocol.store;

public abstract class ErrorCodeStore {
	
	// General
	public static final String GENERAL_SERVER_ERROR = "101";
	public static final String GENERAL_BAD_REQUEST = "102";
	
	// Users
	public static final String REGISTRATION_EMAIL_ALREADY_EXIST = "201";
	public static final String LOGIN_WRONG_EMAIL = "211";
	public static final String LOGIN_WRONG_PASSWORD = "212";
	public static final String AUTHENTICATION_WRONG_TOKEN = "221";
	
	// Friends
	public static final String FIND_FRIEND_USER_NOT_FOUND = "301";
}
