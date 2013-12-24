package com.meetme.protocol.store;

import com.meetme.R;

public abstract class DialogBoxesStore {
	private DialogBoxesStore () {
	}
	
	public static final int VALIDATED_BUTTON = R.string.validatedDialogButton;
	public static final int PLEASE_WAIT = R.string.pleaseWait;
	
	public static final int LOGGING_IN = R.string.loggingIn;
	public static final int LOGGING_IN_SUCCESS_TITLE = R.string.loggingInSuccessDialogTitle;
	public static final int LOGGING_IN_SUCCESS_MESSAGE = R.string.loggingInSuccessDialogMessage;
	
	public static final int REGISTRATION = R.string.registration;
	public static final int REGISTRATION_SUCCESS_TITLE = R.string.registrationSuccessDialogTitle;
	public static final int REGISTRATION_SUCCESS_MESSAGE = R.string.registrationSuccessDialogMessage;
	
}
