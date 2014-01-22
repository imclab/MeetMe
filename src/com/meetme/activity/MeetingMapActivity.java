package com.meetme.activity;

import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meetme.R;


public class MeetingMapActivity extends FragmentActivity implements LocationListener {
	
	GoogleMap googleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_map);
		
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
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
 
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
            String[][] friends = 
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
            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
 
        TextView tvLocation = (TextView) findViewById(R.id.tv_location);
 
        // Getting latitude of the current location
        double latitude = location.getLatitude();
 
        // Getting longitude of the current location
        double longitude = location.getLongitude();
 
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
 
        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
 
        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
 
        // Setting latitude and longitude in the TextView tv_location
        tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );
 
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
    
    
    private void drawMarker(LatLng point, String user, String icon, String eta){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
 
        // Setting options on marker
        markerOptions.position(point);
        BitmapDescriptor marker = null;
        if(icon == "0") {
        	marker = BitmapDescriptorFactory.fromResource(R.drawable.walkmarker);
        }
        else if(icon == "1") {
        	marker = BitmapDescriptorFactory.fromResource(R.drawable.carmarker);
        }
        else if(icon == "2") {
        	marker = BitmapDescriptorFactory.fromResource(R.drawable.bicyclemarker);
        }
        markerOptions.icon(marker);
        markerOptions.title(user);
        markerOptions.snippet(eta);
 
        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
    }
}
