package com.meetme.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

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
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meet;
import com.meetme.model.entity.User;


public class MeetingMapActivity extends FragmentActivity implements LocationListener {
	
	private LocationManager locationManager;
	private GoogleMap googleMap;
	private List<Meet> usersLeftMeetList;
	private Meet userMeet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_map);
		
		// Get meeting presentation 
		usersLeftMeetList = (ArrayList<Meet>)getIntent().getSerializableExtra("usersLeftMeetList");
		userMeet = (Meet)getIntent().getSerializableExtra("userMeet");
		
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
            
            // Add friends as markers
            /*String[][] friends = 
            	{
            		{ "Matthieu K", "43.5912500", "3.8900167", "0", "3 mins"},
            		{ "Baptiste L", "43.6139500", "3.8685333", "2", "10 mins"},
            		{ "Paul V", "43.6012667", "3.8684333", "1", "20 mins"},
            		{ "Benjamen H", "43.6124667", "3.8822333", "0", "1 min"},
            	};
            
            LatLng latLng = null;
            
            for(String[] f : friends)
            {
            	latLng = new LatLng(Double.parseDouble(f[1]), Double.parseDouble(f[2]));
            	drawMarker(latLng, f[0], f[3], f[4]);
            }*/
            
            // Draw friends markers
            SessionManager session = SessionManager.getInstance();
            LatLng latLng = null;
            
            for (Meet meet : usersLeftMeetList) {
            	Friend friend = session.getFriendById(meet.getUserId());
            	
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
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		// Stop GPS updates
		locationManager.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Resume GPS updates
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(provider, 2000, 0, this);
	}
	
    @Override
    public void onLocationChanged(Location location) {
    	// Auto-generated method stub
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
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
}
