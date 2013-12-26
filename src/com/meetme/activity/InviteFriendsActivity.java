package com.meetme.activity;

import static com.meetme.protocol.store.DialogBoxesStore.CREATING_MEETING;
import static com.meetme.protocol.store.DialogBoxesStore.PLEASE_WAIT;
import static com.meetme.protocol.store.ErrorCodeStore.SUCCESS;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_TOKEN;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_CREATE_DATETIME;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_CREATE_DESCRIPTION;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_CREATE_LOCATION_GEO;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_CREATE_FRIENDS;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_CREATE_LOCATION_TEXT;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_CREATE_TITLE;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_OPERATION;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_OPERATION_CREATE;
import static com.meetme.protocol.store.ServerUrlStore.LOGIN_URL;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.meetme.R;
import com.meetme.core.HttpUtils;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meeting;
import com.meetme.presentation.FriendCheckable;
import com.meetme.presentation.FriendListArrayAdapter;
import com.meetme.protocol.HttpParameters;

public class InviteFriendsActivity extends Activity {

	private SessionManager session;
	private Meeting newMeeting;
	private EditText searchFriendEdit;
	private List<FriendCheckable> friendList;
	private FriendListArrayAdapter friendListAdapter;
	private ListView friendListView;
	private Button inviteButton;
	private TextView noFriendText;
	
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
		updateFriendList();
		
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
	 * Update the friend list adapter data
	 */
	private void updateFriendList() {
		friendList.clear();
		
		for (Friend friend : session.getFriendSet()) {
			friendList.add(new FriendCheckable(friend));
		}
	}
	
	private void handleCreateMeetingError(int responseCode) {
		Toast.makeText(getApplicationContext(), "meeting creation error", Toast.LENGTH_SHORT).show();
	}
	
	private void handleCreateMeetingResponse(JSONObject responseJSON) {
		try {
			final int responseCode = responseJSON.getInt("error_code");
			
			if (responseCode == SUCCESS) {
				
				session.addMeeting(newMeeting);
			
				// Start main activity
				Intent i = new Intent(InviteFriendsActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						handleCreateMeetingError(responseCode);
					}
				});
			}
		} catch (JSONException e) {
			Log.d(LoginActivity.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.d(LoginActivity.class.getName(), e.getMessage(), e);
		}
	}
	
	private void createMeeting() {
		final ProgressDialog progressDialog = 
				ProgressDialog.show(InviteFriendsActivity.this, getString(PLEASE_WAIT), getString(CREATING_MEETING), true);
		progressDialog.setCancelable(true);
		
		new Thread(new Runnable() {
			JSONObject responseJSON = null;
			HttpParameters parameters = new HttpParameters();
			
			@Override
			public void run() {
				// Add parameters
				parameters.put(MEETING_TOKEN, session.getUserToken());
				parameters.put(MEETING_OPERATION, MEETING_OPERATION_CREATE);
				parameters.put(MEETING_CREATE_TITLE, newMeeting.getTitle());
				parameters.put(MEETING_CREATE_DESCRIPTION, newMeeting.getDescription());
				parameters.put(MEETING_CREATE_DATETIME, newMeeting.getDatetime());
				parameters.put(MEETING_CREATE_LOCATION_GEO, newMeeting.getLocationGeo());
				parameters.put(MEETING_CREATE_LOCATION_TEXT, newMeeting.getLocationText());
				parameters.put(MEETING_CREATE_FRIENDS, "");
				
				// Send request
				responseJSON = HttpUtils.post(LOGIN_URL, parameters);
			
				// Handle response
				handleCreateMeetingResponse(responseJSON);
				
				progressDialog.dismiss();
			}
		}).start();
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
					createMeeting();
				}
			}
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
