package com.meetme.store;

public abstract class ServerParameterStore {
	private ServerParameterStore() {
	}
	
	private static final String TOKEN = "token";
	private static final String OPERATION = "operation";
	private static final String NOTIFICATION = "notification";
	
	public static final String GOOGLE_MAP_ADDRESS = "address";
	public static final String GOOGLE_MAP_SENSOR = "sensor";
	
	public static final String USER_OPERATION = OPERATION;
	public static final String USER_OPERATION_LOGIN = "login";
	public static final String USER_OPERATION_REGISTER = "register";
	public static final String USER_OPERATION_UPDATE = "update";
	public static final String USER_OPERATION_DELETE = "delete";
	public static final String LOGIN_EMAIL = "email";
	public static final String LOGIN_PASSWORD = "password";
	
	public static final String REGISTRATION_EMAIL = "email";
	public static final String REGISTRATION_FIRSTNAME = "firstname";
	public static final String REGISTRATION_LASTNAME = "lastname";
	public static final String REGISTRATION_PASSWORD = "password";
	
	public static final String MEETING_TOKEN = TOKEN;
	public static final String MEETING_OPERATION = OPERATION;
	public static final String MEETING_OPERATION_CREATE = "create";
	public static final String MEETING_OPERATION_UPDATE = "update";
	public static final String MEETING_OPERATION_DELETE = "delete";
	public static final String MEETING_OPERATION_VIEW = "view";
	public static final String MEETING_OPERATION_LIST = "list";
	public static final String MEETING_OPERATION_NOTIFICATION = NOTIFICATION;
	public static final String MEETING_CREATE_TITLE = "title";
	public static final String MEETING_CREATE_DESCRIPTION = "description";
	public static final String MEETING_CREATE_DATETIME = "datetime";
	public static final String MEETING_CREATE_LOCATION_GEO = "location_geo";
	public static final String MEETING_CREATE_LOCATION_TEXT = "location_text";
	public static final String MEETING_CREATE_FRIENDS = "friends[]";
	public static final String MEETING_VIEW_MEETING_ID = "meeting_id";
	
	public static final String FRIEND_TOKEN = TOKEN;
	public static final String FRIEND_OPERATION = OPERATION;
	public static final String FRIEND_OPERATION_LIST = "list";
	public static final String FRIEND_OPERATION_REQUEST = "request";
	public static final String FRIEND_OPERATION_RESPONSE = "response";
	public static final String FRIEND_OPERATION_FIND_BY_EMAIL = "findByEmail";
	public static final String FRIEND_OPERATION_NOTIFICATION = NOTIFICATION;
	public static final String FRIEND_REQUEST_ID = "friend_id";
	public static final String FRIEND_RESPONSE_RESPONSE_CODE = "response";
	public static final String FRIEND_RESPONSE_ID = "friend_id";
	public static final String FRIEND_FIND_BY_EMAIL_EMAIL = "email";
	
	public static final String MEET_TOKEN = TOKEN;
	public static final String MEET_OPERATION = OPERATION;
	public static final String MEET_OPERATION_CONFIRMATION = "confirmation";
	public static final String MEET_OPERATION_STATUS = "status";
	public static final String MEET_OPERATION_REFRESH = "refresh";
	public static final String MEET_OPERATION_REFRESH_OTHERS = "refresh_others";
	public static final String MEET_CONFIRMATION_CONFIRMATION_CODE = "confirmation_code";
	public static final String MEET_CONFIRMATION_MEETING_ID = "meeting_id";
	public static final String MEET_REFRESH_OTHERS_MEETING_ID = "meeting_id";
	public static final String MEET_STATUS_USER_TRAVEL_MODE = "user_travel_mode";
	public static final String MEET_STATUS_STATUS_CODE = "status_code";
	public static final String MEET_STATUS_MEETING_ID = "meeting_id";
}
