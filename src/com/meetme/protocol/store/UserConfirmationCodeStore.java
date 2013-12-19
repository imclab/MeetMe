package com.meetme.protocol.store;

public abstract class UserConfirmationCodeStore {
	
	public static final String CONFIRMATION_INVITED = "0";
	public static final String CONFIRMATION_ACCEPTED = "1";
	public static final String CONFIRMATION_DECLINED = "2";
	public static final String CONFIRMATION_MAYBE = "3";
}
