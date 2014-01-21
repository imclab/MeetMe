package com.meetme.task;

import static com.meetme.store.DialogBoxesStore.MEETING_ACCEPT_SUCCESS_MESSAGE;
import static com.meetme.store.DialogBoxesStore.MEETING_ACCEPT_SUCCESS_TITLE;
import static com.meetme.store.DialogBoxesStore.OK;
import static com.meetme.store.DialogBoxesStore.PLEASE_WAIT;
import static com.meetme.store.DialogBoxesStore.SENDING_RESPONSE;
import static com.meetme.store.ErrorCodeStore.SUCCESS;
import static com.meetme.store.ServerParameterStore.MEET_CONFIRMATION_CONFIRMATION_CODE;
import static com.meetme.store.ServerParameterStore.MEET_CONFIRMATION_MEETING_ID;
import static com.meetme.store.ServerParameterStore.MEET_OPERATION;
import static com.meetme.store.ServerParameterStore.MEET_OPERATION_CONFIRMATION;
import static com.meetme.store.ServerParameterStore.MEET_TOKEN;
import static com.meetme.store.ServerUrlStore.MEET_URL;
import static com.meetme.store.UserConfirmationCodeStore.USER_CONFIRMATION_ACCEPTED;
import static com.meetme.store.UserConfirmationCodeStore.USER_CONFIRMATION_MAYBE;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.meetme.R;
import com.meetme.activity.LoginActivity;
import com.meetme.activity.NotificationsActivity;
import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.MeetingInviteNotification;

public class MeetingInviteConfirmTask extends AsyncTask<Void, Void, Void> {

	
	private SessionManager session;
	private Context context;
	private MeetingInviteNotification notification;
	private int userConfirmationCode = -1;
	private JSONObject responseJSON = null;
	private HttpParameters parameters = new HttpParameters();
	private ProgressDialog progressDialog;
	private int meetingAcceptState = -1;
	
	public MeetingInviteConfirmTask(
			Context context, 
			MeetingInviteNotification notification, 
			int userConfirmationCode) {
		this.session = SessionManager.getInstance();
		this.userConfirmationCode = userConfirmationCode;
		this.context = context;
		this.notification = notification;
	}
	
	private void handleFriendAcceptResponse(JSONObject responseJSON) {
		try {
			final int responseCode = responseJSON.getInt("error_code");
			
			meetingAcceptState = responseCode;
		} catch (JSONException e) {
			Log.d(LoginActivity.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.d(LoginActivity.class.getName(), e.getMessage(), e);
		}
	}
	
	private void refreshActivity() {
		NotificationsActivity activity = (NotificationsActivity)context;
		activity.refresh();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(
				context, 
				context.getString(PLEASE_WAIT), 
				context.getString(SENDING_RESPONSE), 
				true
			);
		progressDialog.setCancelable(true);
		
		// Add parameters
		parameters.put(MEET_TOKEN, session.getUserToken());
		parameters.put(MEET_OPERATION, MEET_OPERATION_CONFIRMATION);
		parameters.put(MEET_CONFIRMATION_MEETING_ID, Integer.toString(notification.getMeetingId()));
		parameters.put(MEET_CONFIRMATION_CONFIRMATION_CODE, Integer.toString(userConfirmationCode));
		
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// Send request
		responseJSON = HttpUtils.post(MEET_URL, parameters);
		
		// Handle response
		handleFriendAcceptResponse(responseJSON);
		
		if (meetingAcceptState == SUCCESS) {
			// Refresh meeting list
			session.updateMeetingSet();
			
		    // Refresh notification list
			session.updateMeetingNotificationSet();
		}
		
		progressDialog.dismiss();
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// If the user entered accepted or maybe, he's part of the meeting
		boolean userJoinedMeeting = 
				(userConfirmationCode == USER_CONFIRMATION_ACCEPTED)
				|| (userConfirmationCode == USER_CONFIRMATION_MAYBE);
		
		// Build dialog message
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append(context.getString(MEETING_ACCEPT_SUCCESS_MESSAGE)).append(" ");
		messageBuilder.append(notification.getMeetingTitle()).append(".");
		
		if (userJoinedMeeting && meetingAcceptState == SUCCESS) {
			AlertDialog meetingAcceptSuccessInfoDialog = 
					new AlertDialog.Builder(context).create();
 
			meetingAcceptSuccessInfoDialog.setTitle(context.getString(MEETING_ACCEPT_SUCCESS_TITLE));
			meetingAcceptSuccessInfoDialog.setMessage(messageBuilder.toString());
			meetingAcceptSuccessInfoDialog.setIcon(R.drawable.validated_icon);
			meetingAcceptSuccessInfoDialog.setButton(
					AlertDialog.BUTTON_POSITIVE, 
					context.getString(OK),
					meetingAcceptSuccessInfoDialogButtonListener
				);
			meetingAcceptSuccessInfoDialog.show();
		}
		
		refreshActivity();
	}
	
	/*
	 * Listeners
	 */
	
	private DialogInterface.OnClickListener meetingAcceptSuccessInfoDialogButtonListener = 
			new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		refreshActivity();
	        	}
			};
}
