package com.meetme.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meeting;

public class MeetingActivity extends Activity {

	private TextView arrivedText;
	private TextView arrivedList;
	private TextView leftText;
	private TextView leftList;
	private TextView waitingText;
	private TextView waitingList;
	private Meeting meeting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting);
		meeting = (Meeting)getIntent().getSerializableExtra("meeting");
		
		arrivedText = (TextView)findViewById(R.id.arrivedText);
		arrivedText.setOnClickListener(arrivedListener);
		leftText = (TextView)findViewById(R.id.leftText);
		leftText.setOnClickListener(leftListener);
		waitingText = (TextView)findViewById(R.id.waitingText);
		waitingText.setOnClickListener(waitingListener);
		arrivedList = (TextView)findViewById(R.id.arrivedList);
		leftList = (TextView)findViewById(R.id.leftList);
		waitingList = (TextView)findViewById(R.id.waitingList);
		
		init();
	}
	
	/*
	 * Private methods 
	 */
	public void init() {
		runOnUiThread(new Runnable() {
			int arrivedCount = 0;
			int leftCount = 0;
			int waitingCount = 0;
			
			StringBuilder arrivedString = new StringBuilder();
			StringBuilder leftString = new StringBuilder();
			StringBuilder waitingString = new StringBuilder();
			
			@Override
			public void run() {
				Log.d(MeetingActivity.class.getName(), "meeting = " + meeting);
				Log.d(MeetingActivity.class.getName(), "meeting.friendSet() = " + meeting.getFriendSet().isEmpty());
				
				for (Friend friend : meeting.getFriendSet()) {
					int status = (int)(Math.random() * 10) % 3;
					
					Log.d(MeetingActivity.class.getName(), "status = " + status);
					Log.d(MeetingActivity.class.getName(), "friend = " + friend.toString());
					
					if (status == 0) {
						waitingCount++;
						waitingString.append(friend.toString()).append("\n");
					} else if (status == 1) {
						leftCount++;
						leftString.append(friend.toString()).append("\n");
					} else if (status == 2) {
						arrivedCount++;
						arrivedString.append(friend.toString()).append("\n");
					}
				}
				
				arrivedText.setText(getString(R.string.arrivedLabel) + " (" + arrivedCount + ")");
				leftText.setText(getString(R.string.leftLabel) + " (" + leftCount + ")");
				waitingText.setText(getString(R.string.waitingLabel) + " (" + waitingCount + ")");
				
				arrivedList.setText(arrivedString);
				leftList.setText(leftString);
				waitingList.setText(waitingString);
			}
		});
	}
	
	/*
	 * Listeners
	 */
	private void toggleVisibility(View v) {
		int visibility = v.getVisibility();
		v.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
	}
	
	private OnClickListener arrivedListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			toggleVisibility(arrivedList);
		}
	};
	
	private OnClickListener leftListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			toggleVisibility(leftList);
		}
	};
	
	private OnClickListener waitingListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			toggleVisibility(waitingList);
		}
	};
}
