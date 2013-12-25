package com.meetme.protocol.store;

import com.meetme.R;

public abstract class MessageStore {
	private MessageStore() {
	}
	
	public static final int GENERAL_ERROR = R.string.generalError;
	public static final int INVALID_EMAIL = R.string.invalidEmail;
	public static final int INVALID_FIRSTNAME = R.string.invalidFirstname;
	public static final int INVALID_LASTNAME = R.string.invalidLastname;
	public static final int INVALID_PASSWORD = R.string.invalidPassword;
	
	public static final int EMAIL_ALREADY_EXISTS = R.string.emailAlreadyExists;
	public static final int EMPTY_PASSWORD = R.string.emptyPassword;
	public static final int PASSWORDS_DO_NOT_MATCH = R.string.passwordDoNotMatch;
	public static final int EMPTY_TITLE = R.string.emptyTitle;
	public static final int TITLE_TOO_LONG = R.string.titleTooLong;
	
	public static final int WRONG_EMAIL_FULL = R.string.wrongEmailFull;
	public static final int WRONG_EMAIL = R.string.wrongEmail;
	public static final int WRONG_PASSWORD_FULL = R.string.wrongPasswordFull;
	public static final int WRONG_PASSWORD = R.string.wrongPassword;
}
