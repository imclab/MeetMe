package com.meetme.store;

public abstract class UserConfirmationCodeStore {
	private UserConfirmationCodeStore() {
	}
	
	public static final String USER_CONFIRMATION_INVITED = "0";
	public static final String USER_CONFIRMATION_ACCEPTED = "1";
	public static final String USER_CONFIRMATION_DECLINED = "2";
	public static final String USER_CONFIRMATION_MAYBE = "3";
}
