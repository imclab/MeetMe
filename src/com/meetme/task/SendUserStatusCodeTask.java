package com.meetme.task;

import static com.meetme.store.ErrorCodeStore.SUCCESS;
import static com.meetme.store.ServerParameterStore.MEET_OPERATION;
import static com.meetme.store.ServerParameterStore.MEET_OPERATION_STATUS;
import static com.meetme.store.ServerParameterStore.MEET_STATUS_MEETING_ID;
import static com.meetme.store.ServerParameterStore.MEET_STATUS_STATUS_CODE;
import static com.meetme.store.ServerParameterStore.MEET_STATUS_USER_TRAVEL_MODE;
import static com.meetme.store.ServerParameterStore.MEET_TOKEN;
import static com.meetme.store.ServerUrlStore.MEET_URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.meetme.activity.LoginActivity;
import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.core.SessionManager;

public class SendUserStatusCodeTask extends AsyncTask<Void, Void, Void> {
	
	private SessionManager session;
	private JSONObject responseJSON = null;
	private HttpParameters parameters = new HttpParameters();
	private int userStatusCode = -1;
	private int userTravelMode = -1;
	private int meetingId = -1;
	private int sendUserStatusState = -1;
	
	
	public SendUserStatusCodeTask(
			int userStatusCode,
			int userTravelMode, 
			int meetingId) {
		this.session = SessionManager.getInstance();
		this.userStatusCode = userStatusCode;
		this.userTravelMode = userTravelMode;
		this.meetingId = meetingId;
	}
	
	private void handleFriendAcceptResponse(JSONObject responseJSON) {
		try {
			final int responseCode = responseJSON.getInt("error_code");
			
			sendUserStatusState = responseCode;
		} catch (JSONException e) {
			Log.d(LoginActivity.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.d(LoginActivity.class.getName(), e.getMessage(), e);
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		// Add parameters
		parameters.put(MEET_OPERATION, MEET_OPERATION_STATUS);
		parameters.put(MEET_TOKEN, session.getUser().getToken());
		parameters.put(MEET_STATUS_STATUS_CODE, Integer.toString(this.userStatusCode));
		parameters.put(MEET_STATUS_USER_TRAVEL_MODE, Integer.toString(this.userTravelMode));
		parameters.put(MEET_STATUS_MEETING_ID, Integer.toString(this.meetingId));
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// Send request
		responseJSON = HttpUtils.post(MEET_URL, parameters);
		
		// Handle response
		handleFriendAcceptResponse(responseJSON);
		
		if (sendUserStatusState == SUCCESS) {
			// Do nothing
		}
		
		return null;
	}
}
