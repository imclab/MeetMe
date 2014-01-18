package com.meetme.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.dao.MeetDao;
import com.meetme.model.dao.MeetingDao;
import com.meetme.model.entity.Meeting;
import com.meetme.presentation.MeetPresentation;

public class MeetingActivity extends Activity {

	private SessionManager session;
	private TextView arrivedText;
	private TextView arrivedList;
	private TextView leftText;
	private TextView leftList;
	private TextView waitingText;
	private TextView waitingList;
	private Meeting meeting;
	private MeetPresentation meetPresentation;
	
	private MeetingDao meetingDao;
	private MeetDao meetDao;
	
	private static final int REFRESH_RATE = 10000;
	private static boolean REFRESHING = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting);
		meeting = (Meeting)getIntent().getSerializableExtra("meeting");
		
		session = SessionManager.getInstance();
		
		arrivedText = (TextView)findViewById(R.id.arrivedText);
		arrivedText.setOnClickListener(arrivedListener);
		leftText = (TextView)findViewById(R.id.leftText);
		leftText.setOnClickListener(leftListener);
		waitingText = (TextView)findViewById(R.id.waitingText);
		waitingText.setOnClickListener(waitingListener);
		arrivedList = (TextView)findViewById(R.id.arrivedList);
		leftList = (TextView)findViewById(R.id.leftList);
		waitingList = (TextView)findViewById(R.id.waitingList);
		
		meetingDao = new MeetingDao();
		meetDao = new MeetDao();
		
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
	
	private void updateUi() {
		// Update labels
		arrivedText.setText(
    			getString(R.string.arrivedLabel) 
    			+ "(" + meetPresentation.getArrivedCount() + ")");
    	
    	leftText.setText(
    			getString(R.string.leftLabel) 
    			+ "(" + meetPresentation.getLeftCount() + ")");
    	
    	waitingText.setText(
    			getString(R.string.waitingLabel) 
    			+ "(" + meetPresentation.getWaitingCount() + ")");
    	
    	// Update lists
    	arrivedList.setText(meetPresentation.getArrivedString());
    	leftList.setText(meetPresentation.getLeftString());
    	waitingList.setText(meetPresentation.getWaitingString());
    	
    	
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
			                    		meetPresentation = new MeetPresentation(
			                    				meeting,
			                    				meetDao.findMeetsOfMeeting(meeting, session.getUserToken())
		                    				);
			                        	
			                        	// Update UI
			                        	updateUi();
		                        	}
	                            } catch (Exception e) {
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
}
