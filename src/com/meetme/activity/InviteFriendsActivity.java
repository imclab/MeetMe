package com.meetme.activity;

import static com.meetme.protocol.store.ServerParameterStore.*;
import static com.meetme.protocol.store.ServerUrlStore.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.meetme.R;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meeting;
import com.meetme.protocol.HttpParameters;

public class InviteFriendsActivity extends Activity {

	private Set<Friend> friendSet;
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
		
		friendSet = new HashSet<Friend>();
		friendAdapterList = new ArrayList<String>();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, friendAdapterList);
		
		friendListView.setAdapter(adapter);		
		
		getFriendSet();
		updateFriendList();
	}
	
	
	/*
	 * Private methods
	 */
	private void getFriendSet() {
		JSONObject responseJSON = null;
		String url = FRIEND_URL;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_LIST);
		parameters.put(FRIEND_TOKEN, "usr52ac94a27877f4.60168588");
		
		// Send request
		responseJSON = HttpUtils.post(url, parameters);
		
		// Built friend list from JSON response
		try {
			JSONArray meetingsJSON = (JSONArray)responseJSON.get("friends");
			int meetingsSize = meetingsJSON.length();
			
			for (int i = 0; i < meetingsSize; i++) {
				JSONObject meetingJSON = meetingsJSON.getJSONObject(i);
				friendSet.add(Friend.getFromJSON(meetingJSON));
			}
			
		} catch (JSONException e) {
			Log.e(MainActivity.class.getName(), e.getMessage());
		} catch (Exception e) {
			Log.e(MainActivity.class.getName(), e.getMessage());
		}
	}
	
	/*
	 * Update the friend list adapter data
	 */
	private void updateFriendList() {
		friendAdapterList.clear();
		
		for (Friend friend : friendSet) {
			friendAdapterList.add(friend.getFirstname() + "\n" + friend.getLastname());
		}
	}
	
	private OnClickListener inviteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "create meeting", Toast.LENGTH_SHORT).show();
	    }
	};
}
