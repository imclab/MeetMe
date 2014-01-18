package com.meetme.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.FriendInviteNotification;
import com.meetme.model.entity.MeetingInviteNotification;

public class NotificationsActivity extends Activity {
	
	private SessionManager session;
	private List<FriendInviteNotification> friendInviteNotificationList;
	private List<MeetingInviteNotification> meetingInviteNotificationList;
	
	private TextView friendNotificationText;
	private TextView friendNotificationList;
	private TextView meetingNotificationText;
	private TextView meetingNotificationList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		
		friendNotificationText = (TextView)findViewById(R.id.friendNotificationText);
		friendNotificationList = (TextView)findViewById(R.id.friendNotificationList);
		meetingNotificationText = (TextView)findViewById(R.id.meetingNotificationText);
		meetingNotificationList = (TextView)findViewById(R.id.meetingNotificationList);
		
		friendNotificationText.setOnClickListener(friendNotificationTextListener);
		meetingNotificationText.setOnClickListener(meetingNotificationTextListener);
		
		meetingInviteNotificationList = new ArrayList<MeetingInviteNotification>();
		friendInviteNotificationList = new ArrayList<FriendInviteNotification>();
		
		session = SessionManager.getInstance();
		
		updateNotificationsLists();
		
		refresh();
	}
	

	/*
	 * Private methods 
	 */
	private void toggleVisibility(View v) {
		int visibility = v.getVisibility();
		v.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
	}
	
	private void updateNotificationsLists() {
		friendInviteNotificationList.clear();
		meetingInviteNotificationList.clear();
		
		for (FriendInviteNotification friendNotification : session.getFriendNotificationSet()) {
			friendInviteNotificationList.add(friendNotification);
		}
		
		for (MeetingInviteNotification meetingNotification : session.getMeetingNotificationSet()) {
			meetingInviteNotificationList.add(meetingNotification);
		}
	}
	
	private void updateUi() {
		StringBuilder friendNotificationBuilder = new StringBuilder();
		StringBuilder meetingNotificationBuilder = new StringBuilder();
		
		for (FriendInviteNotification friendNotification : friendInviteNotificationList) {
			friendNotificationBuilder.append(friendNotification.getInviterFirstname()).append("\n");
			friendNotificationBuilder.append(friendNotification.getInviterLastname()).append("\n");
			friendNotificationBuilder.append(friendNotification.getDateTime()).append("\n\n");
		}
		
		for (MeetingInviteNotification meetingNotification : meetingInviteNotificationList) {
			meetingNotificationBuilder.append(meetingNotification.getMeetingTitle()).append("\n");
			meetingNotificationBuilder.append(meetingNotification.getMeetingDateTime()).append("\n");
			meetingNotificationBuilder.append("from ").append(session.getFriendById(meetingNotification.getMeetingHostUserId())).append("\n");
			meetingNotificationBuilder.append(meetingNotification.getDateTime()).append("\n\n");
		}
		
		friendNotificationText.setText(getString(R.string.friendNotificationLabel) + "(" + friendInviteNotificationList.size() + ")");
		meetingNotificationText.setText(getString(R.string.meetingNotificationLabel) + "(" + meetingInviteNotificationList.size() + ")");
		
		friendNotificationList.setText(friendNotificationBuilder.toString());
		meetingNotificationList.setText(meetingNotificationBuilder.toString());
	}
	
	private void refresh() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updateUi();
			}
		});
	}
	
	/*
	 * Listeners
	 */
	private OnClickListener friendNotificationTextListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			toggleVisibility(friendNotificationList);
		}
	};
	
	private OnClickListener meetingNotificationTextListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			toggleVisibility(meetingNotificationList);
		}
	};
}
