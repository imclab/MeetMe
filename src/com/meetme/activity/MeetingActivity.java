package com.meetme.activity;

import static com.meetme.store.UserStatusCodeStore.USER_STATUS_ARRIVED;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_LEFT;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_WAITING;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.dao.MeetDao;
import com.meetme.model.dao.MeetingDao;
import com.meetme.model.entity.Meeting;
import com.meetme.presentation.MeetingPresentation;

public class MeetingActivity extends Activity {

	private SessionManager session;
	private Meeting meeting;
	private MeetingPresentation meetPresentation;
	private MeetingDao meetingDao;
	private MeetDao meetDao;
	
	private TextView meetingTitle;
	private TextView meetingDateTime;
	private TextView arrivedLabel;
	private TextView arrivedList;
	private TextView leftLabel;
	private TextView leftList;
	private TextView waitingLabel;
	private TextView waitingList;
	private TextView myStatusText;
	private Button myStatusButton;
	private Button seeMapButton;
	
	private static int MY_STATUS = USER_STATUS_WAITING;
	private static final int REFRESH_RATE = 10000;
	private static boolean REFRESHING = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting);
		meeting = (Meeting)getIntent().getSerializableExtra("meeting");
		
		session = SessionManager.getInstance();
		
		meetingTitle = (TextView)findViewById(R.id.meetingTitle);
		meetingDateTime = (TextView)findViewById(R.id.meetingDateTime);
		arrivedLabel = (TextView)findViewById(R.id.arrivedLabel);
		arrivedLabel.setOnClickListener(arrivedListener);
		leftLabel = (TextView)findViewById(R.id.leftLabel);
		leftLabel.setOnClickListener(leftListener);
		waitingLabel = (TextView)findViewById(R.id.waitingLabel);
		waitingLabel.setOnClickListener(waitingListener);
		arrivedList = (TextView)findViewById(R.id.arrivedList);
		leftList = (TextView)findViewById(R.id.leftList);
		waitingList = (TextView)findViewById(R.id.waitingList);
		myStatusText = (TextView)findViewById(R.id.myStatusText);
		myStatusButton = (Button)findViewById(R.id.myStatusButton);
		myStatusButton.setOnClickListener(myStatusButtonListener);
		seeMapButton = (Button)findViewById(R.id.seeMapButton);
		seeMapButton.setOnClickListener(seeMapButtonListener);
		
		meetingDao = new MeetingDao();
		meetDao = new MeetDao();
		
		updateMeetingInfo();
		refresh();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		// Stop refreshing
		REFRESHING = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// Resume refreshing
		REFRESHING = true;
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
		meetingTitle.setText(meeting.getTitle());
		meetingDateTime.setText(meeting.getDateTime());
	}
	
	private void updateMyStatus() {
		if (MY_STATUS == USER_STATUS_WAITING) {
			myStatusText.setText(R.string.myStatusNotLeftYet);
		} else if (MY_STATUS == USER_STATUS_LEFT){
			myStatusText.setText("2 km\n10 mins\nby foot");
		} else if (MY_STATUS == USER_STATUS_ARRIVED) {
			myStatusText.setText(R.string.myStatusArrived);
		}
	}
	
	private void updateUi() {
		updateMeetingInfo();
		
		// Update labels
		arrivedLabel.setText(
    			getString(R.string.arrivedLabel) 
    			+ " (" + meetPresentation.getArrivedCount() + ")");
    	
    	leftLabel.setText(
    			getString(R.string.leftLabel) 
    			+ " (" + meetPresentation.getLeftCount() + ")");
    	
    	waitingLabel.setText(
    			getString(R.string.waitingLabel) 
    			+ " (" + meetPresentation.getWaitingCount() + ")");
    	
    	// Update lists
    	arrivedList.setText(meetPresentation.getArrivedString());
    	leftList.setText(meetPresentation.getLeftString());
    	waitingList.setText(meetPresentation.getWaitingString());
    	
    	updateMyStatus();
	}
	
	private void refresh() {
		TimerTask doAsynchronousTask;
	    final Handler handler = new Handler();
	    final Timer timer = new Timer(true);
		
		doAsynchronousTask = new TimerTask() {
	    	@Override
	    	public void run() {
	    		handler.post(new Runnable() {
	                        public void run() {
	                        	try {
		                        	if (REFRESHING) {
		                        		int meetingId = meeting.getId();
			                        	
			                        	// TO DO : Refresh meeting data in session
			                        	//session.updateMeeting(meetingId);
			                        	
		                        		// Refresh meeting data
			                        	meeting = meetingDao.findMeetingById(meetingId, session.getUserToken());
			                        	
			                        	// Refresh friends data 
			                    		meetPresentation = new MeetingPresentation(
			                    				meeting,
			                    				meetDao.findMeetsOfMeeting(meeting, session.getUserToken())
		                    				);
			                        	
			                        	// Update UI
			                        	updateUi();
		                        	}
	                            } catch (Exception e) {
	                            	Log.e(MeetingActivity.class.getName(), e.getMessage(), e);
	                            	timer.cancel();
	                            }
	                        }
	                    });
	                }
	            };
	       timer.schedule(doAsynchronousTask, 0, REFRESH_RATE);
	}
	
	/*
	 * Listeners
	 */
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
	
	private OnClickListener seeMapButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MeetingActivity.this, MeetingMapActivity.class);
			startActivity(intent);
		}
	};
	
	private OnClickListener myStatusButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (MY_STATUS == USER_STATUS_WAITING) {
				/*
				 * TODO: Dialog with travel mode choice
				 */
				// If dialog returns response OK
				MY_STATUS = USER_STATUS_LEFT;
				myStatusButton.setText(R.string.myStatusButtonArrive);
			} else if (MY_STATUS == USER_STATUS_LEFT) {
				/*
				 * TODO: Dialog with confirmation of arrival
				 */
				// If dialog returns response OK
				MY_STATUS = USER_STATUS_ARRIVED;
				toggleVisibility(myStatusButton);
			} 
			
			// Send user_status_code task
			
			updateMyStatus();
		}
	};
}
