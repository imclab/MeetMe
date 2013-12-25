package com.meetme.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter.FilterListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.Friend;

public class InviteFriendsActivity extends Activity {

	private SessionManager session;
	private EditText searchFriendEdit;
	private List<String> friendList;
	private ArrayAdapter<String> friendListAdapter;
	private ListView friendListView;
	private Button inviteButton;
	private TextView noFriendText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_friends);
		
		searchFriendEdit = (EditText)findViewById(R.id.searchFriendEdit);
		searchFriendEdit.addTextChangedListener(searchFriendTextWatcher);
		
		friendListView = (ListView)findViewById(R.id.friendList);
		inviteButton = (Button)findViewById(R.id.inviteButton);
		inviteButton.setOnClickListener(inviteListener);
		noFriendText = (TextView)findViewById(R.id.noFriendText);
		
		friendList = new ArrayList<String>();
		
		friendListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, friendList);
		
		friendListView.setAdapter(friendListAdapter);		
		
		session = SessionManager.getInstance();
		updateFriendList();
		
		if (friendList.isEmpty()) {
			noFriendText.setVisibility(View.VISIBLE);
		}
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
			friendList.add(friend.getFirstname() + " " + friend.getLastname());
		}
	}
	
	/*
	 * Listeners
	 */
	private OnClickListener inviteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "create meeting", Toast.LENGTH_SHORT).show();
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
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
}
