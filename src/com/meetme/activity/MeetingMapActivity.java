package com.meetme.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meetme.R;
import com.meetme.core.SessionManager;
import com.meetme.model.dao.GoogleDirectionDao;
import com.meetme.model.dao.MeetDao;
import com.meetme.model.dao.MeetingDao;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.GoogleDirection;
import com.meetme.model.entity.Meet;
import com.meetme.model.entity.Meeting;
import com.meetme.model.entity.User;
import com.meetme.presentation.MeetingPresentation;


public class MeetingMapActivity extends FragmentActivity implements LocationListener {
	
	private SessionManager session;
	private MeetingDao meetingDao;
	private MeetDao meetDao;
	private GoogleDirectionDao googleDirectionDao;
	private LocationManager locationManager;
	private GoogleMap googleMap;
	private List<Meet> usersLeftMeetList;
	private Meeting meeting;
	private Meet userMeet;
	private MeetingPresentation meetingPresentation;
	
	private static boolean REFRESHING = true;
	private static final int REFRESH_RATE = 10000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_map);
		
		session = SessionManager.getInstance();
		meetingDao = new MeetingDao();
		meetDao = new MeetDao();
		googleDirectionDao = new GoogleDirectionDao();
		
		// Get intent extras
		usersLeftMeetList = (ArrayList<Meet>)getIntent().getSerializableExtra("usersLeftMeetList");
		userMeet = (Meet)getIntent().getSerializableExtra("userMeet");
		meeting = (Meeting)getIntent().getSerializableExtra("meeting");
		
		meetingPresentation = new MeetingPresentation(MeetingMapActivity.this, meeting, new TreeSet<Meet>(usersLeftMeetList));
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
 
        // Showing status
        if (status != ConnectionResult.SUCCESS) { 
    	// Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        } else { 
    	// Google Play Services are available
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();
            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);
            // Getting LocationManager object from System Service LOCATION_SERVICE
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);
 
            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
            
            // Draw markers
            drawUser();
            drawFriends();
        }
        
        refresh();
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		// Stop refreshing and GPS updates
		REFRESHING = false;
		locationManager.removeUpdates(this);
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
    private void updateMyLocation(Location location) {
		if (location != null) {
			userMeet.setUserLatitudeLongitude(location.getLatitude() + "," + location.getLongitude());
		} 
		
		Log.d(MeetingMapActivity.class.getName(), "LatLong(" + userMeet.getUserLatitudeLongitude() + ")");
	}
    
    private void drawUser() {
    	// Draw friends markers
        LatLng latLng = null;
        
        latLng = new LatLng(
    			Double.parseDouble(userMeet.getUserLatitudeLongitude().split(",")[0]),
    			Double.parseDouble(userMeet.getUserLatitudeLongitude().split(",")[1])
			);
        
        // Draw user marker
        User user = SessionManager.getInstance().getUser();
        
        drawMarker(
    			latLng, 
    			user.getFirstname() + " " + user.getLastname(), 
    			3, 
    			userMeet.getUserEstimatedTime(),
    			userMeet.getUserEstimatedDistance()
			);
    }
    
    private void drawFriends() {
        LatLng latLng = null;
        
        for (Meet meet : meetingPresentation.getUsersLeftMeetList()) {
        	Friend friend = SessionManager.getInstance().getFriendById(meet.getUserId());
        	
        	latLng = new LatLng(
        			Double.parseDouble(meet.getUserLatitudeLongitude().split(",")[0]),
        			Double.parseDouble(meet.getUserLatitudeLongitude().split(",")[1])
    			);
        	drawMarker(
        			latLng, 
        			friend.getFirstname() + " " + friend.getLastname(), 
        			meet.getUserTravelMode(), 
        			meet.getUserEstimatedTime(),
        			meet.getUserEstimatedDistance()
				);
        }
    }

    private void drawMarker(LatLng point, String user, int icon, String eta, String eda){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
 
        // Setting options on marker
        markerOptions.position(point);
        BitmapDescriptor marker = null;
        if(icon == 0) {
        	marker = BitmapDescriptorFactory.fromResource(R.drawable.walkmarker);
        }
        else if(icon == 1) {
        	marker = BitmapDescriptorFactory.fromResource(R.drawable.carmarker);
        }
        else if(icon == 2) {
        	marker = BitmapDescriptorFactory.fromResource(R.drawable.bicyclemarker);
        } else if (icon == 3) {
        	marker = BitmapDescriptorFactory.fromResource(R.drawable.personalmarker);
        }
        
        markerOptions.icon(marker);
        markerOptions.title(user);
        markerOptions.snippet(eta + " " + eda);
 
        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
    }
    
    private void refresh() {
	final Handler handler = new Handler();
	    
	    Runnable refreshMeeting = new Runnable() {
			@Override
            public void run() {
            	try {
                	if (REFRESHING) {
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
                		
                		// Refresh meeting data
                    	meeting = meetingDao.findMeetingById(meeting.getId(), session.getUser().getToken());
                    	
                    	// Send user Meet and refresh friends data 
                		meetingPresentation = new MeetingPresentation(
                				MeetingMapActivity.this,
                				meeting,
                				meetDao.findAllMeetsOfMeeting(meeting, userMeet, session.getUser().getToken())
            				);
                    	
                		// Get the meet of the user
                		userMeet = meetingPresentation.getUserMeet();
                		
                		// clear map and draw markers
                		googleMap.clear();
                		drawUser();
                		drawFriends();
                		
                    	handler.postDelayed(this, REFRESH_RATE);
                	}
                } catch (Exception e) {
                	Log.e(MeetingActivity.class.getName(), e.getMessage(), e);
                }
            }
	    };
	    
	    handler.post(refreshMeeting);
    }
    
    @Override
    public void onLocationChanged(Location location) {
    	updateMyLocation(location);
    }
 
    @Override
    public void onProviderDisabled(String provider) {
        // Auto-generated method stub
    }
 
    @Override
    public void onProviderEnabled(String provider) {
        // Auto-generated method stub
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Auto-generated method stub
    }
}
