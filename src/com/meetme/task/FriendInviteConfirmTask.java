package com.meetme.task;

import static com.meetme.store.DialogBoxesStore.FRIEND_ACCEPT_SUCCESS_MESSAGE;
import static com.meetme.store.DialogBoxesStore.FRIEND_ACCEPT_SUCCESS_TITLE;
import static com.meetme.store.DialogBoxesStore.OK;
import static com.meetme.store.DialogBoxesStore.PLEASE_WAIT;
import static com.meetme.store.DialogBoxesStore.SENDING_RESPONSE;
import static com.meetme.store.ErrorCodeStore.SUCCESS;
import static com.meetme.store.FriendResponseCodeStore.FRIEND_RESPONSE_ACCEPT;
import static com.meetme.store.FriendResponseCodeStore.FRIEND_RESPONSE_DECLINE;
import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION;
import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION_RESPONSE;
import static com.meetme.store.ServerParameterStore.FRIEND_RESPONSE_ID;
import static com.meetme.store.ServerParameterStore.FRIEND_RESPONSE_RESPONSE_CODE;
import static com.meetme.store.ServerParameterStore.FRIEND_TOKEN;
import static com.meetme.store.ServerUrlStore.FRIEND_URL;

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
import com.meetme.model.entity.FriendInviteNotification;

public class FriendInviteConfirmTask extends AsyncTask<Void, Void, Void> {
	
	private SessionManager session;
	private Context context;
	private FriendInviteNotification notification;
	private boolean accept = false;
	private JSONObject responseJSON = null;
	private HttpParameters parameters = new HttpParameters();
	private ProgressDialog progressDialog;
	private int friendAcceptState = -1;
	
	public FriendInviteConfirmTask(
			Context context, 
			FriendInviteNotification notification, 
			boolean accept) {
		this.session = SessionManager.getInstance();
		this.accept = accept;
		this.context = context;
		this.notification = notification;
	}
	
	private void handleFriendAcceptResponse(JSONObject responseJSON) {
		try {
			final int responseCode = responseJSON.getInt("error_code");
			
			friendAcceptState = responseCode;
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
		parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_RESPONSE);
		parameters.put(FRIEND_TOKEN, session.getUser().getToken());
		parameters.put(FRIEND_RESPONSE_ID, Integer.toString(notification.getInviterId()));
		parameters.put(FRIEND_RESPONSE_RESPONSE_CODE, 
				accept ?
				Integer.toString(FRIEND_RESPONSE_ACCEPT) :
				Integer.toString(FRIEND_RESPONSE_DECLINE)
			);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// Send request
		responseJSON = HttpUtils.post(FRIEND_URL, parameters);
		
		// Handle response
		handleFriendAcceptResponse(responseJSON);
		
		if (friendAcceptState == SUCCESS) {
			// Refresh friend list
			session.updateFriendSet();
			
		    // Refresh notification list
			session.updateFriendgNotificationSet();
		}
		
		progressDialog.dismiss();
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// Build dialog message
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append(notification.getInviterFirstname()).append(" ");
		messageBuilder.append(notification.getInviterLastname()).append(" ");
		messageBuilder.append(context.getString(FRIEND_ACCEPT_SUCCESS_MESSAGE));
		
		if (accept && friendAcceptState == SUCCESS) {
			AlertDialog friendAcceptSuccessInfoDialog = 
					new AlertDialog.Builder(context).create();
 
			friendAcceptSuccessInfoDialog.setTitle(context.getString(FRIEND_ACCEPT_SUCCESS_TITLE));
			friendAcceptSuccessInfoDialog.setMessage(messageBuilder.toString());
			friendAcceptSuccessInfoDialog.setIcon(R.drawable.validated_icon);
			friendAcceptSuccessInfoDialog.setButton(
					AlertDialog.BUTTON_POSITIVE, 
					context.getString(OK),
					friendAcceptSuccessInfoDialogButtonListener
				);
			friendAcceptSuccessInfoDialog.show();
		}
	}
	
	/*
	 * Listeners
	 */
	private DialogInterface.OnClickListener friendAcceptSuccessInfoDialogButtonListener = 
			new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// Refresh the activity
					NotificationsActivity activity = (NotificationsActivity)context;
					activity.refresh();
	        	}
			};
}
