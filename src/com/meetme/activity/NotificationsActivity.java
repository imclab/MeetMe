package com.meetme.activity;

import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.dao.FriendInviteNotificationDao;
import com.meetme.model.entity.FriendInviteNotification;

public class NotificationsActivity extends Activity {
	
	private SessionManager session;
	private FriendInviteNotificationDao friendInviteNotificationDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		
		TextView notificationsText = (TextView)findViewById(R.id.notificationsText);
		
		session = SessionManager.getInstance();
		friendInviteNotificationDao = new FriendInviteNotificationDao();
		
		Set<FriendInviteNotification> friendInviteNotificationSet = 
				friendInviteNotificationDao.findFriendInviteNotifications(session.getUserToken());
		
		StringBuilder notifications = new StringBuilder();
		
		for (FriendInviteNotification friendInviteNotification : friendInviteNotificationSet) {
			notifications.append(friendInviteNotification.getInviterFirstname()).append("\n");
			notifications.append(friendInviteNotification.getInviterLastname()).append("\n");
			notifications.append(friendInviteNotification.getDateTime()).append("\n\n");
		}
		
		notificationsText.setText(notifications.toString());
	}
}
