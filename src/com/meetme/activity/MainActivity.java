package com.meetme.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.meetme.R;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

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
	public void logOut() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
}