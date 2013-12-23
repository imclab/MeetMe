package com.meetme.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.Friend;

public class InviteFriendsActivity extends Activity {

	private SessionManager session;
	private List<String> friendAdapterList;
	private ListView friendListView;
	private Button inviteButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_friends);
		
		friendListView = (ListView)findViewById(R.id.friendList);
		inviteButton = (Button)findViewById(R.id.inviteButton);
		inviteButton.setOnClickListener(inviteListener);
		
		friendAdapterList = new ArrayList<String>();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, friendAdapterList);
		
		friendListView.setAdapter(adapter);		
		
		session = SessionManager.getInstance();
		updateFriendList();
	}
	
	/*
	 * Private methods
	 */
	
	/*
	 * Update the friend list adapter data
	 */
	private void updateFriendList() {
		friendAdapterList.clear();
		
		for (Friend friend : session.getFriendSet()) {
			friendAdapterList.add(friend.getLastname() + "\n" + friend.getFirstname());
		}
	}
	
	private OnClickListener inviteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "create meeting", Toast.LENGTH_SHORT).show();
	    }
	};
}
