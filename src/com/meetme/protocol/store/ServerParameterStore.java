package com.meetme.protocol.store;

public abstract class ServerParameterStore {
	
	public static final String LOGIN_EMAIL = "email";
	public static final String LOGIN_PASSWORD = "password";
	
	public static final String REGISTRATION_EMAIL = "email";
	public static final String REGISTRATION_FIRSTNAME = "firstname";
	public static final String REGISTRATION_LASTNAME = "lastname";
	public static final String REGISTRATION_PASSWORD = "password";
	
	public static final String MEETING_TOKEN = "token";
	public static final String MEETING_OPERATION = "operation";
	public static final String MEETING_OPERATION_CREATE = "create";
	public static final String MEETING_OPERATION_UPDATE = "update";
	public static final String MEETING_OPERATION_DELETE = "delete";
	public static final String MEETING_OPERATION_VIEW = "view";
	public static final String MEETING_OPERATION_LIST = "list";
	public static final String MEETING_CREATE_TITLE = "title";
	public static final String MEETING_CREATE_DESCRIPTION = "description";
	public static final String MEETING_CREATE_DATETIME = "datetime";
	public static final String MEETING_CREATE_LOCATION_GEO = "location_geo";
	public static final String MEETING_CREATE_LOCATION_TEXT = "location_text";
	public static final String MEETING_CREATE_FRIENDS = "friends";
	public static final String MEETING_VIEW_MEETING_ID = "meeting_id";
	
	public static final String FRIEND_TOKEN = "token";
	
	public static final String ADD_FRIEND_TOKEN = "token";
	public static final String ADD_FRIEND_OPERATION = "operation";
	public static final String ADD_FRIEND_OPERATION_ID = "id";
	public static final String ADD_FRIEND_OPERATION_ACCEPT = "accept";
	public static final String ADD_FRIEND_OPERATION_EMAIL = "email";
	public static final String ADD_FRIEND_ID = "friend_id";
	public static final String ADD_FRIEND_ACCEPT = "friend_id";
	public static final String ADD_FRIEND_EMAIL = "email";
	
}
