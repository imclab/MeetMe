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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.Meeting;
import com.meetme.presentation.adapter.MeetingListArrayAdapter;

public class MeetingsActivity extends Activity {

	private SessionManager session;
	private List<Meeting> meetingList;
	private MeetingListArrayAdapter meetingListAdapter;
	private ListView meetingListView;
	private Button newMeetingButton;
	private TextView noMeetingText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meetings);
		
		meetingListView = (ListView)findViewById(R.id.meetingList);
		newMeetingButton = (Button)findViewById(R.id.newMeetingButton);
		newMeetingButton.setOnClickListener(newMeetingListener);
		noMeetingText = (TextView)findViewById(R.id.noMeetingText);
		
		meetingList = new ArrayList<Meeting>();
		
		meetingListView.setOnItemClickListener(meetingListListener);
		
		session = SessionManager.getInstance();
		populateMeetingList();
		
		if (meetingList.isEmpty()) {
			noMeetingText.setVisibility(View.VISIBLE);
		}
		
		meetingListAdapter = new MeetingListArrayAdapter(this, meetingList);
		meetingListView.setAdapter(meetingListAdapter);
	}
	
	/*
	 * Update the meeting list adapter data
	 */
	private void populateMeetingList() {
		meetingList.clear();
		
		for (Meeting meeting : session.getMeetingSet()) {
			meetingList.add(meeting);
		}
	}
	
	/*
	 * Listeners
	 */
	private OnClickListener newMeetingListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MeetingsActivity.this, NewMeetingActivity.class);
			startActivity(intent);
	    }
	};
	
	private OnItemClickListener meetingListListener = new OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(MeetingsActivity.this, MeetingInfoActivity.class);
			intent.putExtra("meeting", meetingList.get(position));
			startActivity(intent);
		}
	};
}
