package com.meetme.activity;

import static com.meetme.store.UserStatusCodeStore.USER_STATUS_ARRIVED;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_LEFT;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_WAITING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_BICYCLING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_DRIVING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_WALKING;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.dao.GoogleDirectionDao;
import com.meetme.model.dao.MeetDao;
import com.meetme.model.dao.MeetingDao;
import com.meetme.model.entity.GoogleDirection;
import com.meetme.model.entity.Meet;
import com.meetme.model.entity.Meeting;
import com.meetme.presentation.MeetingPresentation;
import com.meetme.task.SendUserStatusCodeTask;

public class MeetingActivity extends Activity implements LocationListener {

	private Handler handler;
	private LocationManager locationManager;
	private SessionManager session;
	private Meeting meeting;
	private Meet userMeet;
	private MeetingPresentation meetingPresentation;
	private MeetingDao meetingDao;
	private MeetDao meetDao;
	private GoogleDirectionDao googleDirectionDao;
	
	private TextView meetingTitle;
	private TextView arrivedLabel;
	private TextView arrivedList;
	private TextView leftLabel;
	private TextView leftList;
	private TextView waitingLabel;
	private TextView waitingList;
	private TextView myStatusText;
	private Button myStatusButton;
	private Button seeMapButton;
	
	private static final int REFRESH_RATE = 7500;
	private static boolean REFRESHING = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting);
		
		
		meeting = (Meeting)getIntent().getSerializableExtra("meeting");
		userMeet = new Meet();
		userMeet.setUserTravelMode(TRAVEL_MODE_WALKING);
		
		handler = new Handler();
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		// Get current location
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, REFRESH_RATE, 0, this);
		onLocationChanged(location);
        
		session = SessionManager.getInstance();
        
		meetingTitle = (TextView)findViewById(R.id.meetingTitle);
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
		googleDirectionDao = new GoogleDirectionDao();
		
		updateMeetingInfo();
		refresh();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// Stop refreshing and GPS updates
		locationManager.removeUpdates(this);
		REFRESHING = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// Resume GPS updates
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(provider, REFRESH_RATE, 0, this);
		REFRESHING = true;
	}

	/*
	 * Private methods 
	 */
	private void toggleVisibility(View v) {
		int visibility = v.getVisibility();
		v.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
	}
	
	private void updateMyLocation(Location location) {
		if (location != null) {
			userMeet.setUserLatitudeLongitude(location.getLatitude() + "," + location.getLongitude());
		} 
		
		Log.d(MeetingActivity.class.getName(), "LatLong(" + userMeet.getUserLatitudeLongitude() + ")");
	}
	
	private void sendUserStatusTask() {
		new SendUserStatusCodeTask(
				userMeet.getUserStatus(), 
				userMeet.getUserTravelMode(), 
				meeting.getId()).execute();
	}
	
	private void updateMeetingInfo() {
		meetingTitle.setText(meeting.getTitle());
	}
	
	private void updateMyStatus(
			int userTravelMode,
			int userStatus,
			int myStatusButtonResIdText) {
		userMeet.setUserTravelMode(userTravelMode);
		userMeet.setUserStatus(userStatus);
 	   	myStatusButton.setText(myStatusButtonResIdText);
 	   	
 	   	updateMyStatusUi();
	}
	
	private void updateMyStatusUi() {
		String estimatedDistance = userMeet.getUserEstimatedDistance(); 
		String estimatedTime = userMeet.getUserEstimatedTime();
		String myTravelMode = meetingPresentation.getTravelModeString(userMeet.getUserTravelMode());
		String inTimeOrLate = meetingPresentation.getUserInTimeOrLate(userMeet.getUserEstimatedTimeSeconds());
		
		// Build my status messags
		StringBuilder myStatusBuilder = new StringBuilder();
		myStatusBuilder.append(estimatedDistance).append("\n");
		myStatusBuilder.append(estimatedTime).append("\n");
		myStatusBuilder.append(myTravelMode).append("\n");
		myStatusBuilder.append(inTimeOrLate);
		
		if (userMeet.getUserStatus() == USER_STATUS_WAITING) {
			myStatusText.setText(R.string.myStatusNotLeftYet);
			myStatusButton.setText(R.string.myStatusButtonLeave);
		} else if (userMeet.getUserStatus() == USER_STATUS_LEFT){
			myStatusText.setText(myStatusBuilder.toString());
			myStatusButton.setText(R.string.myStatusButtonArrive);
		} else if (userMeet.getUserStatus() == USER_STATUS_ARRIVED) {
			myStatusText.setText(R.string.myStatusArrived);
			myStatusButton.setVisibility(View.GONE);
		}
	}
	
	private void updateGuestsUi() {
		// Update labels
		arrivedLabel.setText(
    			getString(R.string.arrivedLabel) 
    			+ " (" + meetingPresentation.getArrivedCount() + ")");
    	
    	leftLabel.setText(
    			getString(R.string.leftLabel) 
    			+ " (" + meetingPresentation.getLeftCount() + ")");
    	
    	waitingLabel.setText(
    			getString(R.string.waitingLabel) 
    			+ " (" + meetingPresentation.getWaitingCount() + ")");
    	
    	// Update lists
    	arrivedList.setText(meetingPresentation.getArrivedString());
    	leftList.setText(meetingPresentation.getLeftString());
    	waitingList.setText(meetingPresentation.getWaitingString());
	}
	
	private void updateUi() {
		updateMeetingInfo();
		updateGuestsUi();
    	updateMyStatusUi();
	}
	
	private void refresh() {
	    
		Runnable refreshMeeting = new Runnable() {
			@Override
            public void run() {
            	try {
                	if (REFRESHING) {
                		int meetingId = meeting.getId();
                		
                		// Get estimation time and direction
                		GoogleDirection googleDirection = googleDirectionDao.findBetweenUserLocationAndMeeting(
                				userMeet.getUserLatitudeLongitude(), 
                				userMeet.getUserTravelMode(), 
                				meeting.getLocationGeo()
                				);
                		
                		// update user Meet
                		userMeet.setUserEstimatedDistance(googleDirection.getUserEda());
                		userMeet.setUserEstimatedTime(googleDirection.getUserEta());
                		userMeet.setUserEstimatedTimeSeconds(googleDirection.getUserEtaSeconds());
                		
                    	// TO DO : Refresh meeting data in session
                    	//session.updateMeeting(meetingId);
                		
                		// TO DO : if status arrived then only refresh_others 
                		
                		// Refresh meeting data
                    	meeting = meetingDao.findMeetingById(meetingId, session.getUser().getToken());
                    	
                    	// Send user Meet and refresh friends data 
                		meetingPresentation = new MeetingPresentation(
                				MeetingActivity.this,
                				meeting,
                				meetDao.findAllMeetsOfMeeting(meeting, userMeet, session.getUser().getToken())
            				);
                    	
                		// Get the meet of the user
                		userMeet = meetingPresentation.getUserMeet();
                		
                    	// Update UI
                    	runOnUiThread(new Runnable() {
	                    		@Override
                    			public void run() {
	                    			updateUi();
	                    		}
                    		});
                    	 
                    	handler.postDelayed(this, REFRESH_RATE);
                	}
                } catch (Exception e) {
                	Log.e(MeetingActivity.class.getName(), e.getMessage(), e);
                }
            }
	    };
	    
	    handler.post(refreshMeeting);
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
			intent.putExtra("userMeet", userMeet);
			intent.putExtra("meeting", meeting);
			intent.putExtra("usersLeftMeetList", meetingPresentation.getUsersLeftMeetList());
			startActivity(intent);
		}
	};
	
	private OnClickListener myStatusButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (userMeet.getUserStatus() == USER_STATUS_WAITING) {
				// Build dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(MeetingActivity.this);
				builder.setTitle(R.string.travelModeDialogTitle);
				builder.setMessage(R.string.travelModeDialogMessage);
				builder.setPositiveButton(R.string.byCar, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   updateMyStatus(TRAVEL_MODE_DRIVING, USER_STATUS_LEFT, R.string.myStatusButtonArrive);
			        	   sendUserStatusTask();
			           }
			       });
				 
				builder.setNeutralButton(R.string.byBicycle, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   updateMyStatus(TRAVEL_MODE_BICYCLING, USER_STATUS_LEFT, R.string.myStatusButtonArrive);
			        	   sendUserStatusTask();
			           }
			       });
				
				builder.setNegativeButton(R.string.byFoot, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   updateMyStatus(TRAVEL_MODE_WALKING, USER_STATUS_LEFT, R.string.myStatusButtonArrive);
			        	   sendUserStatusTask();
			           }
			       });
				
				AlertDialog dialog = builder.create();
				dialog.show();
			} else if (userMeet.getUserStatus() == USER_STATUS_LEFT) {
				// Build dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(MeetingActivity.this);
				builder.setTitle(R.string.confirmationArrivedDialogTitle);
				builder.setMessage(R.string.confirmationArrivedDialogMessage);
				builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   userMeet.setUserStatus(USER_STATUS_ARRIVED);
			        	   sendUserStatusTask();
			        	   myStatusText.setText(R.string.myStatusArrived);
			        	   toggleVisibility(myStatusButton);
			           }
			       });
				
				builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			           }
			       });
				
				AlertDialog dialog = builder.create();
				dialog.show();
			} 
		}
	};
	
	/*
	 * Implementation of LocationListener 
	 */
	@Override
	public void onLocationChanged(Location location) {
		updateMyLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
