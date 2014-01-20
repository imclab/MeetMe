package com.meetme.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.FriendInviteNotification;
import com.meetme.model.entity.MeetingInviteNotification;
import com.meetme.presentation.adapter.FriendInviteNotificationListArrayAdapter;
import com.meetme.presentation.adapter.MeetingInviteNotificationListArrayAdapter;

public class NotificationsActivity extends Activity {
	
	private static final int DEFAULT_NOTIFICATION_SPINNER_POSITION = 1;
	
	private SessionManager session;
	private List<FriendInviteNotification> friendInviteNotificationList;
	private List<MeetingInviteNotification> meetingInviteNotificationList;
	private FriendInviteNotificationListArrayAdapter friendInviteNotificationAdapter;
	private MeetingInviteNotificationListArrayAdapter meetingInviteNotificationAdapter;
	private Spinner pickNotificationTypeSpinner;
	private TextView friendNotificationText;
	private ListView friendNotificationListView;
	private TextView meetingNotificationText;
	private ListView meetingNotificationListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		
		pickNotificationTypeSpinner = (Spinner)findViewById(R.id.pickNotificationTypeSpinner);
		friendNotificationText = (TextView)findViewById(R.id.noFriendNotificationText);
		friendNotificationListView = (ListView)findViewById(R.id.friendNotificationList);
		meetingNotificationText = (TextView)findViewById(R.id.noMeetingNotificationText);
		meetingNotificationListView = (ListView)findViewById(R.id.meetingNotificationList);
		
		pickNotificationTypeSpinner.setSelection(DEFAULT_NOTIFICATION_SPINNER_POSITION);
		
		friendNotificationListView.setOnItemClickListener(friendNotificationListViewListener);
		meetingNotificationListView.setOnItemClickListener(meetingNotificationListViewListener);
		pickNotificationTypeSpinner.setOnItemSelectedListener(pickNotificationTypeSpinnerListener);
		
		meetingInviteNotificationList = new ArrayList<MeetingInviteNotification>();
		friendInviteNotificationList = new ArrayList<FriendInviteNotification>();
		
		session = SessionManager.getInstance();
		
		// Update lists
		populateNotificationsLists();
		
		// Set adapaters
		friendInviteNotificationAdapter = 
				new FriendInviteNotificationListArrayAdapter(
						this,
						friendInviteNotificationList
					);
	    friendNotificationListView.setAdapter(friendInviteNotificationAdapter);
		
		meetingInviteNotificationAdapter = 
				new MeetingInviteNotificationListArrayAdapter(
						this,
						meetingInviteNotificationList
					);
	    meetingNotificationListView.setAdapter(meetingInviteNotificationAdapter);
	    
		refresh();
	}

	/*
	 * Private methods 
	 */
	private void displayFriendNotifications() {
		if (friendInviteNotificationList.isEmpty()) {
			friendNotificationText.setVisibility(View.VISIBLE);
			friendNotificationListView.setVisibility(View.GONE);
		} else {
			friendNotificationText.setVisibility(View.GONE);
			friendNotificationListView.setVisibility(View.VISIBLE);
		}
		
		meetingNotificationText.setVisibility(View.GONE);
		meetingNotificationListView.setVisibility(View.GONE);
	}
	
	private void displayMeetingNotifications() {
		if (meetingInviteNotificationList.isEmpty()) {
			meetingNotificationText.setVisibility(View.VISIBLE);
			meetingNotificationListView.setVisibility(View.GONE);
		} else {
			meetingNotificationText.setVisibility(View.GONE);
			meetingNotificationListView.setVisibility(View.VISIBLE);
		}
		
		friendNotificationText.setVisibility(View.GONE);
		friendNotificationListView.setVisibility(View.GONE);
	}
	
	private void displayFriendNotificationResponseDialog(int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Friend request");
		builder.setMessage("firstname lastname would like to be your friend");
		builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   // send accept
	        	   
	        	   //   add friend in session
	        	   //	delete notification from session
	        	   //	display dialog : firstname lastname is now in your friend list
	           }
	       });
		builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   // send decline
	           }
	       });
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void displayMeetingNotificationResponseDialog(int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Meeting invitation");
		builder.setMessage("firstname lastname would like to invite you in the meeting meetingTitle");
		builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   // send accept
	        	   
	        	   //	add meeting in session
	        	   //	delete notification from session
	        	   //	go to meeting activity
	           }
	       });
		
		builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   // send decline
	        	   // delete notification from session
	           }
	       });
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void populateNotificationsLists() {
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
	private OnItemSelectedListener pickNotificationTypeSpinnerListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if (pos == 1) {
				displayMeetingNotifications();
			} else {
				displayFriendNotifications();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			parent.setSelection(1);
		}
	};
	
	private OnItemClickListener friendNotificationListViewListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				displayFriendNotificationResponseDialog(position);
			}
		};
	
	private OnItemClickListener meetingNotificationListViewListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				displayMeetingNotificationResponseDialog(position);
			}
		};
}
