package com.meetme.activity;

import static com.meetme.store.UserStatusCodeStore.USER_STATUS_ARRIVED;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_LEFT;
import static com.meetme.store.UserStatusCodeStore.USER_STATUS_WAITING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_BICYCLING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_DRIVING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_WALKING;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.dao.MeetDao;
import com.meetme.model.dao.MeetingDao;
import com.meetme.model.entity.Meeting;
import com.meetme.presentation.MeetingPresentation;
import com.meetme.task.SendUserStatusCodeTask;

public class MeetingActivity extends Activity implements LocationListener {

	private LocationManager locationManager;
	private SessionManager session;
	private Meeting meeting;
	private MeetingPresentation meetingPresentation;
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
	
	private static double MY_LATITUTE = -1;
	private static double MY_LONGITUDE = -1;
	private static int MY_TRAVEL_MODE = TRAVEL_MODE_WALKING;
	private static int MY_STATUS = USER_STATUS_WAITING;
	private static final int REFRESH_RATE = 10000;
	private static boolean REFRESHING = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting);
		meeting = (Meeting)getIntent().getSerializableExtra("meeting");
		
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 
				2000, 
				0, 
				this
			);
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
		
		updateMyLocation(null);
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
		
		// Resume refreshing and GPS updates
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 
				2000, 
				0, 
				this
			);
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
			MY_LONGITUDE = location.getLongitude();
			MY_LATITUTE = location.getLatitude();
		}
		
		String locationString = "gps(" + location.getLatitude() + "," + location.getLongitude() + ")\n";
		locationString += "local(" + MY_LATITUTE + "," + MY_LONGITUDE + ")";
		Toast.makeText(getApplicationContext(), locationString, Toast.LENGTH_LONG).show();
	}
	
	private void updateMeetingInfo() {
		// build meeting host text
		meetingTitle.setText(meeting.getTitle());
		meetingDateTime.setText(meeting.getDateTime());
	}
	
	private void sendUserStatusTask() {
		new SendUserStatusCodeTask(
				MY_STATUS, 
				MY_TRAVEL_MODE, 
				meeting.getId()).execute();
	}
	
	private void updateMyStatus(
			int userTravelMode,
			int userStatus,
			int myStatusButtonResIdText) {
		MY_TRAVEL_MODE = userTravelMode;
 	   	MY_STATUS = userStatus;
 	   	myStatusButton.setText(myStatusButtonResIdText);
 	   	
 	   	updateMyStatusUi();
	}
	
	private void updateMyStatusUi() {
		// Build my status message
		String estimatedDistance = "2 km";
		String estimatedTime = "10 mins";
		String myTravelMode = meetingPresentation.getTravelModeString(MY_TRAVEL_MODE);
		
		StringBuilder myStatusBuilder = new StringBuilder();
		myStatusBuilder.append(estimatedDistance).append("\n");
		myStatusBuilder.append(estimatedTime).append("\n");
		myStatusBuilder.append(myTravelMode);
		
		if (MY_STATUS == USER_STATUS_WAITING) {
			myStatusText.setText(R.string.myStatusNotLeftYet);
		} else if (MY_STATUS == USER_STATUS_LEFT){
			myStatusText.setText(myStatusBuilder.toString());
		} else if (MY_STATUS == USER_STATUS_ARRIVED) {
			myStatusText.setText(R.string.myStatusArrived);
		}
	}
	
	private void updateUi() {
		updateMeetingInfo();
		
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
    	
    	updateMyStatusUi();
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
		                        		
		                        		updateMyLocation(null);
		                        		
			                        	// TO DO : Refresh meeting data in session
			                        	//session.updateMeeting(meetingId);
			                        	
		                        		// Refresh meeting data
			                        	meeting = meetingDao.findMeetingById(meetingId, session.getUser().getToken());
			                        	
			                        	// Refresh friends data 
			                    		meetingPresentation = new MeetingPresentation(
			                    				MeetingActivity.this,
			                    				meeting,
			                    				meetDao.findMeetsOfMeeting(meeting, session.getUser().getToken())
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
			} else if (MY_STATUS == USER_STATUS_LEFT) {
				// Build dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(MeetingActivity.this);
				builder.setTitle(R.string.confirmationArrivedDialogTitle);
				builder.setMessage(R.string.confirmationArrivedDialogMessage);
				builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   MY_STATUS = USER_STATUS_ARRIVED;
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
