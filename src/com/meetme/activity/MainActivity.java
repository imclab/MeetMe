package com.meetme.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.meetme.R;
import com.meetme.model.database.DatabaseHandler;
import com.meetme.model.database.Session;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	private LocationManager locationManager;
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tabHost = (TabHost)findViewById(android.R.id.tabhost);
		
		TabSpec tabMeetings = tabHost.newTabSpec("Meetings tab");
		TabSpec tabNotifications = tabHost.newTabSpec("Notifications tab");
        TabSpec tabFindFriends = tabHost.newTabSpec("Find friends tab");
		
        tabMeetings.setIndicator(
        		getString(R.string.tabMeetings), 
        		getResources().getDrawable(R.drawable.tab_meetings)
			);
        tabMeetings.setContent(new Intent(this, MeetingsActivity.class));
        
        tabNotifications.setIndicator(
        		getString(R.string.tabNotifications), 
        		getResources().getDrawable(R.drawable.tab_notifications)
			);
        tabNotifications.setContent(new Intent(this, NotificationsActivity.class));

        tabFindFriends.setIndicator(
        		getString(R.string.tabFindFriends),
        		getResources().getDrawable(R.drawable.tab_find_friends)
			);
        tabFindFriends.setContent(new Intent(this, FindFriendsActivity.class));
        
        tabHost.addTab(tabMeetings);
        tabHost.addTab(tabNotifications);
        tabHost.addTab(tabFindFriends);
        
        // Change tabs background color
        /*TabWidget tw = getTabWidget();

        for (int i = 0; i < tw.getChildCount(); i++) {
                    View v = tw.getChildAt(i);
                    v.setBackgroundResource(R.drawable.tab_background);
        }*/
        
        // Check if GPS is enabled
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showGPSDisabledAlert();
        } 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.logOutMenuAction:
	            logOut();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/*
	 * Private methods
	 */
	private void showGPSDisabledAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.gpsIsdisabledDialogMessage);
        alertDialogBuilder.setCancelable(false);
        
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        
        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
	
	public void logOut() {
		DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
		Session session = databaseHandler.getSession();
		
		// A persistent session exists
		if (session != null) {
			databaseHandler.deleteSession(session);
		}
		
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
}