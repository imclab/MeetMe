package com.meetme.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.meetme.R;

public class MainActivity extends TabActivity {

	private TabHost tabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tabHost = (TabHost)findViewById(android.R.id.tabhost);
		
		TabSpec tabMeetings = tabHost.newTabSpec("Meetings tab");
		TabSpec tabNewMeeting = tabHost.newTabSpec("New meeting tab");
        TabSpec tabFindFriends = tabHost.newTabSpec("Find friends tab");
		
        tabMeetings.setIndicator(
        		getString(R.string.tabMeetings), 
        		getResources().getDrawable(R.drawable.ic_launcher)
			);
        tabMeetings.setContent(new Intent(this, MeetingsActivity.class));
        
        tabNewMeeting.setIndicator(
        		getString(R.string.tabNewMeeting), 
        		getResources().getDrawable(R.drawable.ic_launcher)
			);
        tabNewMeeting.setContent(new Intent(this, NewMeetingActivity.class));

        tabFindFriends.setIndicator(
        		getString(R.string.tabFindFriends),
        		getResources().getDrawable(R.drawable.search_icon)
			);
        tabFindFriends.setContent(new Intent(this, FindFriendsActivity.class));
        
        tabHost.addTab(tabMeetings);
        tabHost.addTab(tabNewMeeting);
        tabHost.addTab(tabFindFriends);
	}
}