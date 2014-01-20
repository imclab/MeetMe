package com.meetme.activity;

import static com.meetme.store.DialogBoxesStore.MEETING_CREATION;
import static com.meetme.store.DialogBoxesStore.MEETING_CREATION_ERROR_MESSAGE;
import static com.meetme.store.DialogBoxesStore.MEETING_CREATION_ERROR_TITLE;
import static com.meetme.store.DialogBoxesStore.MEETING_CREATION_SUCCESS_MESSAGE;
import static com.meetme.store.DialogBoxesStore.MEETING_CREATION_SUCCESS_TITLE;
import static com.meetme.store.DialogBoxesStore.OK;
import static com.meetme.store.DialogBoxesStore.PLEASE_WAIT;
import static com.meetme.store.ErrorCodeStore.SUCCESS;
import static com.meetme.store.ServerParameterStore.MEETING_CREATE_DATETIME;
import static com.meetme.store.ServerParameterStore.MEETING_CREATE_DESCRIPTION;
import static com.meetme.store.ServerParameterStore.MEETING_CREATE_FRIENDS;
import static com.meetme.store.ServerParameterStore.MEETING_CREATE_LOCATION_GEO;
import static com.meetme.store.ServerParameterStore.MEETING_CREATE_LOCATION_TEXT;
import static com.meetme.store.ServerParameterStore.MEETING_CREATE_TITLE;
import static com.meetme.store.ServerParameterStore.MEETING_OPERATION;
import static com.meetme.store.ServerParameterStore.MEETING_OPERATION_CREATE;
import static com.meetme.store.ServerParameterStore.MEETING_TOKEN;
import static com.meetme.store.ServerUrlStore.MEETING_URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter.FilterListener;
import android.widget.ListView;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meeting;
import com.meetme.presentation.FriendCheckable;
import com.meetme.presentation.adapter.FriendListArrayAdapter;

public class InviteFriendsActivity extends Activity {

	private SessionManager session;
	private Meeting newMeeting;
	private EditText searchFriendEdit;
	private List<FriendCheckable> friendList;
	private FriendListArrayAdapter friendListAdapter;
	private ListView friendListView;
	private Button inviteButton;
	private TextView noFriendText;
	
	// To keep track of the meeting creation process
	private static int meetingCreationState = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_friends);
		
		newMeeting = (Meeting)getIntent().getSerializableExtra("newMeeting");
		
		searchFriendEdit = (EditText)findViewById(R.id.searchFriendEdit);
		searchFriendEdit.addTextChangedListener(searchFriendTextWatcher);
		friendListView = (ListView)findViewById(R.id.friendList);
		inviteButton = (Button)findViewById(R.id.inviteButton);
		inviteButton.setOnClickListener(inviteListener);
		noFriendText = (TextView)findViewById(R.id.noFriendText);
		
		friendList = new ArrayList<FriendCheckable>();
		
		session = SessionManager.getInstance();
		populateFriendList();
		
		if (friendList.isEmpty()) {
			noFriendText.setVisibility(View.VISIBLE);
		}
		
		friendListAdapter = new FriendListArrayAdapter(this, friendList);
		friendListView.setAdapter(friendListAdapter);
	}
	
	/*
	 * Private methods
	 */
	
	/*
	 * Populate the friend list adapter data
	 */
	private void populateFriendList() {
		friendList.clear();
		
		for (Friend friend : session.getFriendSet()) {
			friendList.add(new FriendCheckable(friend));
		}
	}
	
	private void handleCreateMeetingResponse(JSONObject responseJSON) {
		try {
			final int responseCode = responseJSON.getInt("error_code");
			
			meetingCreationState = responseCode;
		} catch (JSONException e) {
			Log.d(LoginActivity.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.d(LoginActivity.class.getName(), e.getMessage(), e);
		}
	}
	
	private void createMeeting() {
		new MeetingCreationProcess().execute();
	}
	
	/*
	 * Meeting creation task
	 */
	private class MeetingCreationProcess extends AsyncTask<Void, Void, Void> {
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		ProgressDialog progressDialog;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            meetingCreationState = -1;
            progressDialog = ProgressDialog.show(InviteFriendsActivity.this, getString(PLEASE_WAIT), getString(MEETING_CREATION), true);
			progressDialog.setCancelable(true);
        }
 
        @Override
        protected Void doInBackground(Void...voids) {
        	// Add parameters
			parameters.put(MEETING_TOKEN, session.getUserToken());
			parameters.put(MEETING_OPERATION, MEETING_OPERATION_CREATE);
			parameters.put(MEETING_CREATE_TITLE, newMeeting.getTitle());
			parameters.put(MEETING_CREATE_DESCRIPTION, newMeeting.getDescription());
			parameters.put(MEETING_CREATE_DATETIME, Long.toString(newMeeting.getTimestamp()));
			parameters.put(MEETING_CREATE_LOCATION_GEO, newMeeting.getLocationGeo());
			parameters.put(MEETING_CREATE_LOCATION_TEXT, newMeeting.getLocationText());
			
			Set<Friend> friendSet = newMeeting.getFriendSet();
			
			if (!friendSet.isEmpty()) {
				for (Friend friend : friendSet) {
					parameters.put(MEETING_CREATE_FRIENDS, Integer.toString(friend.getId()));
				}
			}
			
			// Send request
			responseJSON = HttpUtils.post(MEETING_URL, parameters);
		
			// Handle response
			handleCreateMeetingResponse(responseJSON);
			
			progressDialog.dismiss();
			
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
    		if (meetingCreationState == SUCCESS) {
    			// Add meeting in session
    			session.addMeeting(newMeeting);
    			
				// Show success dialog
				AlertDialog meetingCreationSuccessInfoDialog = 
						new AlertDialog.Builder(InviteFriendsActivity.this).create();

				meetingCreationSuccessInfoDialog.setTitle(getString(MEETING_CREATION_SUCCESS_TITLE));
				meetingCreationSuccessInfoDialog.setMessage(getString(MEETING_CREATION_SUCCESS_MESSAGE));
				meetingCreationSuccessInfoDialog.setIcon(R.drawable.validated_icon);
				meetingCreationSuccessInfoDialog.setButton(
						AlertDialog.BUTTON_POSITIVE, 
						getString(OK),
						meeetingCreationInfoDialogButtonListener
					);
				meetingCreationSuccessInfoDialog.show();
    		} else {
    			// Show error dialog
    			AlertDialog meetingCreationErrorInfoDialog = 
    					new AlertDialog.Builder(InviteFriendsActivity.this).create();

    			meetingCreationErrorInfoDialog.setTitle(getString(MEETING_CREATION_ERROR_TITLE));
    			meetingCreationErrorInfoDialog.setMessage(getString(MEETING_CREATION_ERROR_MESSAGE));
    			meetingCreationErrorInfoDialog.setIcon(R.drawable.error_icon);
    			meetingCreationErrorInfoDialog.setButton(
    					AlertDialog.BUTTON_POSITIVE, 
    					getString(OK),
    					meeetingCreationInfoDialogButtonListener
    				);
    			meetingCreationErrorInfoDialog.show();
    		}
        }
    }
	
	/*
	 * Listeners
	 */
	private OnClickListener inviteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			for (FriendCheckable friend : friendListAdapter.getFriendList()) {
				if (friend.isSelected()) {
					newMeeting.addFriend(friend);
				}
			}
			
			createMeeting();
	    }
	};
	
	private DialogInterface.OnClickListener meeetingCreationInfoDialogButtonListener = 
			new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		Intent i = new Intent(InviteFriendsActivity.this, MainActivity.class);
					startActivity(i);
					finish();
	        	}
			};
	
	/*
	 * Toggle the visibility of the textView in case of no result is found
	 */
	private FilterListener searchFriendFilterListener = new FilterListener() {
		@Override
		public void onFilterComplete(int count) {
			if (count == 0) {
				noFriendText.setVisibility(View.VISIBLE);
			} else {
				noFriendText.setVisibility(View.INVISIBLE);
			}
		}
	};
	
	/*
	 * Apply a filter on the displayed friend list 
	 * every time the search box text is modified
	 */
	private TextWatcher searchFriendTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			friendListAdapter.getFilter().filter(s, searchFriendFilterListener);
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			//Do nothing before text changed
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			//Do nothing after text changed
		}
	};
}
