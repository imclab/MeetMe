package com.meetme.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.meetme.R;
import com.meetme.model.entity.Meeting;
import com.meetme.validator.NewMeetingValidator;

public class NewMeetingActivity extends Activity {

	private EditText titleEdit;
	private EditText descriptionEdit;
	private EditText dateTimeEdit;
	private EditText locationEdit;
	private Button inviteFriendsButton;
	private NewMeetingValidator newMeetingValidator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_meeting);
		
		titleEdit = (EditText)findViewById(R.id.titleEdit);
		descriptionEdit = (EditText)findViewById(R.id.descriptionEdit);
		dateTimeEdit = (EditText)findViewById(R.id.dateTimeEdit);
		locationEdit = (EditText)findViewById(R.id.locationEdit);
		inviteFriendsButton = (Button)findViewById(R.id.inviteFriendsButton);
		inviteFriendsButton.setOnClickListener(inviteFriendsListener);
		
		newMeetingValidator = new NewMeetingValidator(
				getApplicationContext(), 
				titleEdit
			); 
	}
	
	/*
	 * Private methods
	 */
	private Meeting createMeeting() {
		Meeting meeting = new Meeting();
		meeting.setTitle(titleEdit.getText().toString());
		meeting.setDescription("Let's get fucking drunk");
		meeting.setDatetime("2014-01-14 21:30:00");
		meeting.setLocationGeo("75,45");
		meeting.setLocationText("Le Corum");
		
		return meeting;
	}
	
	/*
	 * Listeners
	 */
	private OnClickListener inviteFriendsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (newMeetingValidator.validate()) {
				// Start invite friends activity
				Intent intent = new Intent(NewMeetingActivity.this, InviteFriendsActivity.class);
				intent.putExtra("newMeeting", createMeeting());
				startActivity(intent);
			}
	    }
	};
}
