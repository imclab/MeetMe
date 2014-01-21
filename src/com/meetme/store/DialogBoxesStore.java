package com.meetme.store;

import com.meetme.R;

public abstract class DialogBoxesStore {
	private DialogBoxesStore () {
	}
	
	public static final int OK = R.string.ok;
	public static final int CANCEL = R.string.cancel;
	public static final int YES = R.string.yes;
	public static final int NO = R.string.no;
	public static final int ACCEPT = R.string.accept;
	public static final int DECLINE = R.string.decline;
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
	
	public static final int CONFIRM_LOCATION_TITLE = R.string.confirmLocationDialogTitle;
	
	public static final int SENDING_RESPONSE = R.string.sendingResponse;
	public static final int FRIEND_REQUEST_CONFIRM_DIALOG_TITLE = R.string.confirmFriendRequestDialogTitle;
	public static final int FRIEND_REQUEST_CONFIRM_DIALOG_MESSAGE = R.string.confirmFriendRequestDialogMessage;
	public static final int FRIEND_ACCEPT_SUCCESS_TITLE = R.string.friendAcceptSuccessDialogTitle;
	public static final int FRIEND_ACCEPT_SUCCESS_MESSAGE = R.string.friendAcceptSuccessDialogMessage;
	public static final int MEETING_INVITATION_CONFIRM_DIALOG_TITLE = R.string.confirmMeetingInvitationDialogTitle;
	public static final int MEETING_INVITATION_CONFIRM_DIALOG_MESSAGE = R.string.confirmMeetingInvitationDialogMessage;
	public static final int MEETING_ACCEPT_SUCCESS_TITLE = R.string.meetingAcceptSuccessDialogTitle;
	public static final int MEETING_ACCEPT_SUCCESS_MESSAGE = R.string.meetingAcceptSuccessDialogMessage;
}
