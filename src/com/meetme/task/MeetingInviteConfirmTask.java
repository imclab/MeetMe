package com.meetme.task;

import static com.meetme.store.DialogBoxesStore.MEETING_ACCEPT_SUCCESS_MESSAGE;
import static com.meetme.store.DialogBoxesStore.MEETING_ACCEPT_SUCCESS_TITLE;
import static com.meetme.store.DialogBoxesStore.OK;
import static com.meetme.store.DialogBoxesStore.PLEASE_WAIT;
import static com.meetme.store.DialogBoxesStore.SENDING_RESPONSE;
import static com.meetme.store.ErrorCodeStore.SUCCESS;
import static com.meetme.store.ServerUrlStore.MEETING_URL;

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
	private boolean accept = false;
	private JSONObject responseJSON = null;
	private HttpParameters parameters = new HttpParameters();
	private ProgressDialog progressDialog;
	private int meetingAcceptState = -1;
	
	public MeetingInviteConfirmTask(
			Context context, 
			MeetingInviteNotification notification, 
			boolean accept) {
		this.session = SessionManager.getInstance();
		this.accept = accept;
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
		/*parameters.put(MEETING_TOKEN, session.getUserToken());
		parameters.put(MEETING_ACCEPT, Integer.toString(notification.getInviterId()));
		
		if (accept) {
			parameters.put(MEETING_OPERATION, MEETING_OPERATION_ACCEPT);
		} else {
			parameters.put(MEETING_OPERATION, MEETING_OPERATION_DECLINE);
		}*/
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// Send request
		responseJSON = HttpUtils.post(MEETING_URL, parameters);
		
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
		// Build dialog message
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append(context.getString(MEETING_ACCEPT_SUCCESS_MESSAGE)).append(" ");
		messageBuilder.append(notification.getMeetingTitle()).append(".");
		
		if (accept && meetingAcceptState == SUCCESS) {
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
	}
	
	/*
	 * Listeners
	 */
	
	private DialogInterface.OnClickListener meetingAcceptSuccessInfoDialogButtonListener = 
			new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// Refresh the activity
					NotificationsActivity activity = (NotificationsActivity)context;
					activity.refresh();
	        	}
			};
}
