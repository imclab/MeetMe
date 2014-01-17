package com.meetme.store;

public abstract class ServerUrlStore {
	private ServerUrlStore() {
	}
	
	private static final String HOST_URL = "http://meetme.whitefenix.co/api/";
	
	public static final String USER_URL = HOST_URL + "user.php";
	public static final String MEETING_URL = HOST_URL + "meeting.php";
	public static final String FRIEND_URL = HOST_URL + "friend.php";
	public static final String MEET_URL = HOST_URL + "meet.php";
	
	public static final String GOOGLE_MAP_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
}
