package com.meetme.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.Meeting;

public class MainActivity extends Activity {

	private SessionManager session;
	private List<String> meetingAdapterList;
	private ListView meetingListView;
	private Button newMeetingButton;
	private Button findFriendsButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		meetingListView = (ListView)findViewById(R.id.meetingList);
		newMeetingButton = (Button)findViewById(R.id.newMeetingButton);
		newMeetingButton.setOnClickListener(newMeetingListener);
		findFriendsButton = (Button)findViewById(R.id.findFriendsButton);
		findFriendsButton.setOnClickListener(findFriendsListener);
		
		meetingAdapterList = new ArrayList<String>();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, meetingAdapterList);
		
		meetingListView.setAdapter(adapter);		
		
		meetingListView.setOnItemClickListener(meetingListListener);
		
		session = SessionManager.getInstance();
		updateMeetingList();
	}
	
	/*
	 * Private methods 
	 */
	
	/*
	 * Update the meeting list adapter data
	 */
	private void updateMeetingList() {
		meetingAdapterList.clear();
		
		for (Meeting meeting : session.getMeetingSet()) {
			meetingAdapterList.add(meeting.getTitle() + "\n" + meeting.getDatetime());
		}
	}
	
	/*
	 * Listeners
	 */
	private OnClickListener newMeetingListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, NewMeetingActivity.class);
			startActivity(intent);
	    }
	};
	
	private OnClickListener findFriendsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, FindFriendsActivity.class);
			startActivity(intent);
	    }
	};
	
	private OnItemClickListener meetingListListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// MOCK UP LISTENER, TO REPLACE WITH CUSTOM ADAPTER
			Intent intent = new Intent(MainActivity.this, MeetingActivity.class);
			intent.putExtra("meeting", session.getMeetingById(33));
			startActivity(intent);
		}
	};
}
