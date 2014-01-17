package com.meetme.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
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
}