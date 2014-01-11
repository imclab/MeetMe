package com.meetme.activity;

import static com.meetme.protocol.store.DialogBoxesStore.PLEASE_WAIT;
import static com.meetme.protocol.store.DialogBoxesStore.SEARCHING_USER;
import static com.meetme.protocol.store.DialogBoxesStore.SENDING_INVITATION;
import static com.meetme.protocol.store.ErrorCodeStore.*;
import static com.meetme.protocol.store.MessageStore.*;
import static com.meetme.protocol.store.ServerParameterStore.FRIEND_ADD_ID;
import static com.meetme.protocol.store.ServerParameterStore.FRIEND_FIND_BY_EMAIL_EMAIL;
import static com.meetme.protocol.store.ServerParameterStore.FRIEND_OPERATION;
import static com.meetme.protocol.store.ServerParameterStore.FRIEND_OPERATION_ADD;
import static com.meetme.protocol.store.ServerParameterStore.FRIEND_OPERATION_FIND_BY_EMAIL;
import static com.meetme.protocol.store.ServerParameterStore.FRIEND_TOKEN;
import static com.meetme.protocol.store.ServerUrlStore.FRIEND_URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.HttpUtils;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.Friend;
import com.meetme.protocol.HttpParameters;
import com.meetme.validator.FindFriendsValidator;

public class FindFriendsActivity extends Activity {

	private SessionManager session;
	private Friend foundFriend;
	private EditText searchFriendEdit;
	private TextView foundFriendText;
	private Button addFriendButton;
	private Button findButton;
	private FindFriendsValidator findFriendsValidator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_friends);
		
		searchFriendEdit = (EditText)findViewById(R.id.searchFriendEdit);
		foundFriendText = (TextView)findViewById(R.id.foundFriendText);
		addFriendButton = (Button)findViewById(R.id.addFriendButton);
		addFriendButton.setOnClickListener(addFriendListener);
		findButton = (Button)findViewById(R.id.findButton);
		findButton.setOnClickListener(findListener);
		
		session = SessionManager.getInstance();
		
		findFriendsValidator = new FindFriendsValidator(getApplicationContext(), searchFriendEdit);
	}
	
	/*
	 * Private methods
	 */
	private void handleAddFriendResponse(JSONObject responseJSON) {
		
	}
	
	private void addFriend() {
		final ProgressDialog progressDialog = 
				ProgressDialog.show(FindFriendsActivity.this, getString(PLEASE_WAIT), getString(SENDING_INVITATION), true);
		progressDialog.setCancelable(true);
		
		new Thread(new Runnable() {
			JSONObject responseJSON = null;
			HttpParameters parameters = new HttpParameters();
			
			@Override
			public void run() {
				// Add parameters
				parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_ADD);
				parameters.put(FRIEND_ADD_ID, Integer.toString(foundFriend.getId()));
				parameters.put(FRIEND_TOKEN, session.getUserToken());
				
				// Send request
				responseJSON = HttpUtils.post(FRIEND_URL, parameters);
			
				// Handle response
				handleAddFriendResponse(responseJSON);
				
				progressDialog.dismiss();
			}
		}).start();
	}
	
	private void handleLoginError(int errorCode) {
		switch (errorCode) {
			case FIND_FRIEND_USER_NOT_FOUND : 
				this.searchFriendEdit.setError(getString(NO_FRIEND_FOUND_FOR_EMAIL));
			break;
			case FIND_FRIEND_ALREADY_FRIEND :
				this.searchFriendEdit.setError(getString(FRIEND_ALREADY_IN_LIST));
			break;
			case FIND_FRIEND_USER_IS_YOURSELF :
				this.searchFriendEdit.setError(getString(FRIEND_IS_YOURSELF));
			break;
			case FIND_FRIEND_INVITATION_ALREADY_SENT :
				this.searchFriendEdit.setError(getString(FRIEND_INVITATION_ALREADY_SENT));
			break;
			default :
				this.searchFriendEdit.setError(getString(GENERAL_ERROR));
			break;
		}
	}
	
	private void handleFindFriendResponse(JSONObject responseJSON) {
		try {
			final int responseCode = responseJSON.getInt("error_code");
			
			if (responseCode == SUCCESS) {
				foundFriend = Friend.getFromJSON(responseJSON.getJSONObject("user"));
				
				runOnUiThread(new Runnable() {
					public void run() {
						foundFriendText.setText(
								"Firstname : " + foundFriend.getFirstname() + "\n"
								+ "Lastname : " + foundFriend.getLastname()
								);
						foundFriendText.setVisibility(View.VISIBLE);
						addFriendButton.setVisibility(View.VISIBLE);
					}
				});
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						handleLoginError(responseCode);
					}
				});
			}
		} catch (JSONException e) {
			Log.d(FindFriendsActivity.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.d(FindFriendsActivity.class.getName(), e.getMessage(), e);
		}
	}
	
	private void findFriend() {
		final ProgressDialog progressDialog = 
				ProgressDialog.show(FindFriendsActivity.this, getString(PLEASE_WAIT), getString(SEARCHING_USER), true);
		progressDialog.setCancelable(true);
		
		new Thread(new Runnable() {
			JSONObject responseJSON = null;
			HttpParameters parameters = new HttpParameters();
			
			@Override
			public void run() {
				// Add parameters
				parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_FIND_BY_EMAIL);
				parameters.put(FRIEND_FIND_BY_EMAIL_EMAIL, searchFriendEdit.getText().toString());
				parameters.put(FRIEND_TOKEN, session.getUserToken());
				
				// Send request
				responseJSON = HttpUtils.post(FRIEND_URL, parameters);
			
				// Handle response
				handleFindFriendResponse(responseJSON);
				
				progressDialog.dismiss();
			}
		}).start();
	}
	
	/*
	 * Listeners
	 */
	private OnClickListener addFriendListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			addFriend();
		}
	};
	
	private OnClickListener findListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (findFriendsValidator.validate()) {
				findFriend();
			}
		}
	};
}
