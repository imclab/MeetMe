package com.meetme.protocol.store;

public abstract class ErrorCodeStore {
	public static final int SUCCESS = 0;
	
	// General
	public static final int GENERAL_SERVER_ERROR = 101;
	public static final int GENERAL_BAD_REQUEST = 102;
	
	// Users
	public static final int REGISTRATION_EMAIL_ALREADY_EXIST = 201;
	public static final int LOGIN_WRONG_EMAIL = 211;
	public static final int LOGIN_WRONG_PASSWORD = 212;
	public static final int AUTHENTICATION_WRONG_TOKEN = 221;
	
	// Friends
	public static final int FIND_FRIEND_USER_NOT_FOUND = 301;
}
