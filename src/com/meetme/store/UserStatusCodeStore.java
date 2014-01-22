package com.meetme.store;

public abstract class UserStatusCodeStore {
	
	private UserStatusCodeStore() {
	}
	
	public static final int USER_STATUS_WAITING = 0;
	public static final int USER_STATUS_LEFT = 1;
	public static final int USER_STATUS_ARRIVED = 2;
}
