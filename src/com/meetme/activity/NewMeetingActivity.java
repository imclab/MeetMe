package com.meetme.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.meetme.R;
import com.meetme.core.HttpUtils;
import com.meetme.protocol.HttpParameters;
import com.meetme.validator.LoginValidator;
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
		
		newMeetingValidator = new NewMeetingValidator(titleEdit); 
	}
	
	/*
	 * Private methods
	 */
	private OnClickListener inviteFriendsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Start registration activity
			Intent intent = new Intent(NewMeetingActivity.this, InviteFriendsActivity.class);
			startActivity(intent);
	    }
	};
}
