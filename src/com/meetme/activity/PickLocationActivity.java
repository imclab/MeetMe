package com.meetme.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.meetme.R;
import com.meetme.model.entity.Meeting;

public class PickLocationActivity extends Activity {

	private Meeting newMeeting;
	private Button inviteFriendsButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_location);
		
		newMeeting = (Meeting)getIntent().getSerializableExtra("newMeeting");
		inviteFriendsButton = (Button)findViewById(R.id.inviteFriendsButton);
		inviteFriendsButton.setOnClickListener(inviteFriendsListener);
	}
	
	/*
	 * Private methods
	 */
	private void addLocationToMeeting() {
		newMeeting.setLocationGeo("75,45");
		newMeeting.setLocationText("Le Corum");
	}
	
	/*
	 * Listeners
	 */
	private OnClickListener inviteFriendsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// add location to the new meeting
			addLocationToMeeting();
			
			// Start invite friends activity
			Intent intent = new Intent(PickLocationActivity.this, InviteFriendsActivity.class);
			intent.putExtra("newMeeting", newMeeting);
			startActivity(intent);
		}
	};
}
