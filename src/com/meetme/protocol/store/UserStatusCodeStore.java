package com.meetme.protocol.store;

public abstract class UserStatusCodeStore {
	private UserStatusCodeStore() {
	}
	
	public static final String USER_STATUS_WAITING = "0";
	public static final String USER_STATUS_LEFT = "1";
	public static final String USER_STATUS_ARRIVED = "2";
}
