package com.meetme.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

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
        		getResources().getDrawable(R.drawable.tab_meetings)
			);
        tabMeetings.setContent(new Intent(this, MeetingsActivity.class));
        
        tabNewMeeting.setIndicator(
        		getString(R.string.tabNewMeeting), 
        		getResources().getDrawable(R.drawable.tab_new_meeting)
			);
        tabNewMeeting.setContent(new Intent(this, NewMeetingActivity.class));

        tabFindFriends.setIndicator(
        		getString(R.string.tabFindFriends),
        		getResources().getDrawable(R.drawable.tab_find_friends)
			);
        tabFindFriends.setContent(new Intent(this, FindFriendsActivity.class));
        
        tabHost.addTab(tabMeetings);
        tabHost.addTab(tabNewMeeting);
        tabHost.addTab(tabFindFriends);
        
        // Change tabs background color
        /*TabWidget tw = getTabWidget();

        for (int i = 0; i < tw.getChildCount(); i++) {
                    View v = tw.getChildAt(i);
                    v.setBackgroundResource(R.drawable.tab_background);
        }*/
	}
}