package com.meetme.store;

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
	
	public static final int MEETING_CREATION = R.string.meetingCreation;
	public static final int MEETING_CREATION_SUCCESS_TITLE = R.string.meetingCreationSuccessDialogTitle;
	public static final int MEETING_CREATION_SUCCESS_MESSAGE = R.string.meetingCreationSuccessDialogMessage;
	public static final int MEETING_CREATION_ERROR_TITLE = R.string.meetingCreationErrorDialogTitle;
	public static final int MEETING_CREATION_ERROR_MESSAGE = R.string.meetingCreationErrorDialogMessage;
	
	public static final int SEARCHING_USER = R.string.searchingUser;
	public static final int SENDING_INVITATION = R.string.sendingInvitation;
}
