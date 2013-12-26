package com.meetme.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		meetingListView = (ListView)findViewById(R.id.meetingList);
		newMeetingButton = (Button)findViewById(R.id.newMeetingButton);
		newMeetingButton.setOnClickListener(newMeetingListener);
		
		meetingAdapterList = new ArrayList<String>();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, meetingAdapterList);
		
		meetingListView.setAdapter(adapter);		
		
		session = SessionManager.getInstance();
		updateMeetingList();
	}
	
	/*
	 * Update the meeting list adapter data
	 */
	private void updateMeetingList() {
		meetingAdapterList.clear();
		
		for (Meeting meeting : session.getMeetingSet()) {
			meetingAdapterList.add(meeting.getTitle() + "\n" + meeting.getDatetime());
		}
	}
	
	private void newMeeting() {
		// Start new meeting activity
		Intent intent = new Intent(MainActivity.this, NewMeetingActivity.class);
		startActivity(intent);
	}
	
	private OnClickListener newMeetingListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			newMeeting();
	    }
	};
	
	
}
