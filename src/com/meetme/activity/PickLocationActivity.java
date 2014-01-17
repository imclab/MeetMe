package com.meetme.activity;

import static com.meetme.store.DialogBoxesStore.CONFIRM_LOCATION_TITLE;
import static com.meetme.store.DialogBoxesStore.NO;
import static com.meetme.store.DialogBoxesStore.YES;
import static com.meetme.store.ServerParameterStore.GOOGLE_MAP_ADDRESS;
import static com.meetme.store.ServerParameterStore.GOOGLE_MAP_SENSOR;
import static com.meetme.store.ServerUrlStore.GOOGLE_MAP_URL;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.meetme.R;
import com.meetme.core.map.DownloadTask;
import com.meetme.core.map.MarkerUtils;
import com.meetme.model.entity.Meeting;
import com.meetme.validator.PickLocationValidator;

public class PickLocationActivity extends FragmentActivity {
	
	private Meeting newMeeting;
	private String locationText;
	private String locationGeo;
	public GoogleMap map;
    private Button findLocationButton;
    private SupportMapFragment mapFragment;
    private EditText findLocationEdit;
	private PickLocationValidator pickLocationValidator;
	
	private static final String NEW_MEETING_INTENT_KEY = "newMeeting";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_location);
		
		newMeeting = (Meeting)getIntent().getSerializableExtra(NEW_MEETING_INTENT_KEY);
		
        findLocationEdit = (EditText) findViewById(R.id.findLocationEdit);
        
        findLocationButton = (Button) findViewById(R.id.findLocationButton);
        findLocationButton.setOnClickListener(findLocationButtonListener);
        
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        map.setOnMarkerClickListener(mapListener);
		
		pickLocationValidator = new PickLocationValidator(getApplicationContext(), findLocationEdit);
	}
	
	/*
	 * Private methods
	 */
	private void addLocationToMeeting() {
		newMeeting.setLocationText(locationText);
		newMeeting.setLocationGeo(locationGeo);
		
		// Start invite friends activity
		Intent intent = new Intent(PickLocationActivity.this, InviteFriendsActivity.class);
		intent.putExtra(NEW_MEETING_INTENT_KEY, newMeeting);
		startActivity(intent);
	}
	
	private void showConfirmLocationDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(CONFIRM_LOCATION_TITLE);
		builder.setMessage(locationText);
		builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	            addLocationToMeeting();
	           }
	       });
		builder.setNegativeButton(NO, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	           }
	       });
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/*
	 * Listeners
	 */
	private OnClickListener findLocationButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
        	if (pickLocationValidator.validate()) {
	            // Getting the place entered
	            String location = findLocationEdit.getText().toString();
	            String url = GOOGLE_MAP_URL;
	
	            try {
	                // encoding special characters like space in the user input place
	                location = URLEncoder.encode(location, "utf-8");
	            } catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
	            }
	
	            String address = GOOGLE_MAP_ADDRESS + "=" + location;
	            String sensor = GOOGLE_MAP_SENSOR + "=false";
	
	            // url , from where the geocoding data is fetched
	            url = url + address + "&" + sensor;
	
	            // Instantiating DownloadTask to get places from Google Geocoding service
	            // in a non-ui thread
	            DownloadTask downloadTask = new DownloadTask(map);
	
	            // Start downloading the geocoding places
	            downloadTask.execute(url);
        	}
        }
    };
    
    private OnMarkerClickListener mapListener = new OnMarkerClickListener() {
		
		@Override
		public boolean onMarkerClick(Marker marker) {
			locationText = MarkerUtils.getLocationTextFromMarker(marker);
			locationGeo = MarkerUtils.getLocationGeoFromMarker(marker);
			showConfirmLocationDialog();
			return false;
		}
	};
}
