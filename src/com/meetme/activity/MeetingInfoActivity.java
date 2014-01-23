package com.meetme.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.dao.MeetDao;
import com.meetme.model.dao.MeetingDao;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meeting;
import com.meetme.presentation.MeetingPresentation;

public class MeetingInfoActivity extends Activity {
	
	private SessionManager session;
	private Friend host;
	private Meeting meeting;
	private MeetingPresentation meetingPresentation;
	private MeetingDao meetingDao;
	private MeetDao meetDao;
	
	private TextView meetingTitle;
	private TextView meetingDateTime;
	private TextView meetingLocation;
	private TextView meetingDescription;
	private TextView meetingHost;
	private TextView goingLabel;
	private TextView goingList;
	private TextView maybeLabel;
	private TextView maybeList;
	private TextView invitedLabel;
	private TextView invitedList;
	private TextView declinedLabel;
	private TextView declinedList;
	private Button seeLiveButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_info);
		
		meeting = (Meeting)getIntent().getSerializableExtra("meeting");
		session = SessionManager.getInstance();
		host = session.getFriendById(meeting.getHostUserId());
		
		meetingTitle = (TextView)findViewById(R.id.meetingTitle);
		meetingDateTime = (TextView)findViewById(R.id.meetingDateTime);
		meetingLocation = (TextView)findViewById(R.id.meetingLocation);
		meetingDescription = (TextView)findViewById(R.id.meetingDescription);
		meetingHost = (TextView)findViewById(R.id.meetingHost);
		
		goingLabel = (TextView)findViewById(R.id.meetingGoingLabel);
		goingLabel.setOnClickListener(goingLabelListener);
		maybeLabel = (TextView)findViewById(R.id.meetingMaybeLabel);
		maybeLabel.setOnClickListener(maybeLabelListener);
		invitedLabel = (TextView)findViewById(R.id.meetingInvitedLabel);
		invitedLabel.setOnClickListener(invitedLabelListener);
		declinedLabel = (TextView)findViewById(R.id.meetingDeclinedLabel);
		declinedLabel.setOnClickListener(declinedLabelListener);
		goingList = (TextView)findViewById(R.id.meetingGoingList);
		maybeList = (TextView)findViewById(R.id.meetingMaybeList);
		invitedList = (TextView)findViewById(R.id.meetingInvitedList);
		declinedList = (TextView)findViewById(R.id.meetingDeclinedList);
		
		seeLiveButton = (Button)findViewById(R.id.seeLiveButton);
		seeLiveButton.setOnClickListener(seeLiveButtonListener);
		
		meetingDao = new MeetingDao();
		meetDao = new MeetDao();
		
		updateMeetingInfo();
		loadMeeting();
	}
	
	/*
	 * Private methods
	 */
	private void toggleVisibility(View v) {
		int visibility = v.getVisibility();
		v.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
	}
	
	private void updateMeetingInfo() {
		// build meeting host text
		StringBuilder meetingHostBuilder = new StringBuilder();
		meetingHostBuilder.append(getString(R.string.meetingHostLabel)).append(" ");
		meetingHostBuilder.append(host.getFirstname()).append(" ").append(host.getLastname());
		
		meetingTitle.setText(meeting.getTitle());
		meetingDateTime.setText(meeting.getDateTime());
		meetingLocation.setText(meeting.getLocationText());
		meetingDescription.setText(
				meeting.getDescription().isEmpty() ?
				getString(R.string.noDescription) :
				meeting.getDescription()
			);
		meetingHost.setText(meetingHostBuilder.toString());
	}
	
	private void updateUi() {
		updateMeetingInfo();
		
		// update users infos
		goingLabel.setText(getString(R.string.goingLabel) + " (" + meetingPresentation.getGoingCount() + ")");
		goingList.setText(meetingPresentation.getGoingString());
		maybeLabel.setText(getString(R.string.maybeLabel) + " (" + meetingPresentation.getMaybeCount() + ")");
		maybeList.setText(meetingPresentation.getMaybeString());
		invitedLabel.setText(getString(R.string.invitedLabel) + " (" + meetingPresentation.getInvitedCount() + ")");
		invitedList.setText(meetingPresentation.getInvitedString());
		declinedLabel.setText(getString(R.string.declinedLabel) + " (" + meetingPresentation.getDeclinedCount() + ")");
		declinedList.setText(meetingPresentation.getDeclinedString());
	}
	
	private void loadMeeting() {
		new Thread(new Runnable() {
            public void run() {
            	try {
                		int meetingId = meeting.getId();
                    	
                    	// TO DO : Refresh meeting data in session
                    	//session.updateMeeting(meetingId);
                    	
                		// Load meeting data
                    	meeting = meetingDao.findMeetingById(meetingId, session.getUser().getToken());
                    	
                    	// Load friends data 
                		meetingPresentation = new MeetingPresentation(
                				MeetingInfoActivity.this,
                				meeting,
                				meetDao.findOtherMeetsOfMeeting(meeting, session.getUser().getToken())
            				);
                    	
                		runOnUiThread(new Runnable() {
                			@Override
							public void run() {
                				updateUi();
							}
                		});
                	} catch (Exception e) {
                		Log.e(MeetingInfoActivity.class.getName(), e.getMessage(), e);
                	}
            }
        }).start();
	}
	
	/*
	 * Listeners
	 */
	private OnClickListener goingLabelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			toggleVisibility(goingList);
		}
	};
	
	private OnClickListener maybeLabelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			toggleVisibility(maybeList);
		}
	};
	
	private OnClickListener invitedLabelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			toggleVisibility(invitedList);
		}
	};
	
	private OnClickListener declinedLabelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			toggleVisibility(declinedList);
		}
	};
	
	private OnClickListener seeLiveButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MeetingInfoActivity.this, MeetingActivity.class);
			intent.putExtra("meeting", meeting);
			startActivity(intent);
		}
	};
}
