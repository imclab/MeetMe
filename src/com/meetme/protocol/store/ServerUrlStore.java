package com.meetme.protocol.store;

public abstract class ServerUrlStore {
	
	private static final String HOST_URL = "http://meetme.whitefenix.co/api/";
	
	public static final String REGISTRATION_URL = HOST_URL + "register.php";
	public static final String LOGIN_URL = HOST_URL + "login.php";
	public static final String MEETING_URL = HOST_URL + "meeting.php";
	public static final String FRIEND_URL = HOST_URL + "friend.php";
	public static final String ADD_FRIEND_URL = HOST_URL + "addfriend.php";
}
