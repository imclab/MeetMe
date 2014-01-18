package com.meetme.activity;

import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.dao.FriendInviteNotificationDao;
import com.meetme.model.dao.MeetingInviteNotificationDao;
import com.meetme.model.entity.FriendInviteNotification;
import com.meetme.model.entity.MeetingInviteNotification;

public class NotificationsActivity extends Activity {
	
	private SessionManager session;
	private Set<FriendInviteNotification> friendInviteNotificationSet;
	private Set<MeetingInviteNotification> meetingInviteNotificationSet;
	private FriendInviteNotificationDao friendInviteNotificationDao;
	private MeetingInviteNotificationDao meetingInviteNotificationDao;
	
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
		
		session = SessionManager.getInstance();
		friendInviteNotificationDao = new FriendInviteNotificationDao();
		meetingInviteNotificationDao = new MeetingInviteNotificationDao();
		
		friendInviteNotificationSet = 
				friendInviteNotificationDao.findFriendInviteNotifications(session.getUserToken());
		meetingInviteNotificationSet = 
				meetingInviteNotificationDao.findMeetingInviteNotifications(session.getUserToken());
	
		refresh();
	}
	

	/*
	 * Private methods 
	 */
	private void toggleVisibility(View v) {
		int visibility = v.getVisibility();
		v.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
	}
	
	private void updateUi() {
		StringBuilder friendNotificationBuilder = new StringBuilder();
		StringBuilder meetingNotificationBuilder = new StringBuilder();
		
		for (FriendInviteNotification friendNotification : friendInviteNotificationSet) {
			friendNotificationBuilder.append(friendNotification.getInviterFirstname()).append("\n");
			friendNotificationBuilder.append(friendNotification.getInviterLastname()).append("\n");
			friendNotificationBuilder.append(friendNotification.getDateTime()).append("\n\n");
		}
		
		for (MeetingInviteNotification meetingNotification : meetingInviteNotificationSet) {
			meetingNotificationBuilder.append(meetingNotification.getMeetingTitle()).append("\n");
			meetingNotificationBuilder.append(meetingNotification.getMeetingDateTime()).append("\n");
			meetingNotificationBuilder.append("from ").append(session.getFriendById(meetingNotification.getMeetingHostUserId())).append("\n");
			meetingNotificationBuilder.append(meetingNotification.getDateTime()).append("\n\n");
		}
		
		friendNotificationText.setText(getString(R.string.friendNotificationLabel) + "(" + friendInviteNotificationSet.size() + ")");
		meetingNotificationText.setText(getString(R.string.meetingNotificationLabel) + "(" + meetingInviteNotificationSet.size() + ")");
		
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
