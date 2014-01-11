package com.meetme.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.meetme.R;
import com.meetme.core.DateUtils;
import com.meetme.model.entity.Meeting;
import com.meetme.validator.NewMeetingValidator;

public class NewMeetingActivity extends Activity {

	private EditText titleEdit;
	private EditText descriptionEdit;
	private EditText dateTimeEdit;
	private EditText locationEdit;
	private Button inviteFriendsButton;
	private Button dateTimeChooseButton;
	AlertDialog dateTimeDialog;
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
		dateTimeChooseButton = (Button)findViewById(R.id.dateTimeChooseButton);
		dateTimeChooseButton.setOnClickListener(dateTimeChooseListener);
		
		initDateTimeDialog();
		
		newMeetingValidator = new NewMeetingValidator(
				getApplicationContext(), 
				titleEdit
			); 
	}
	
	/*
	 * Private methods
	 */
	private void initDateTimeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(getLayoutInflater().inflate(R.layout.datetime_dialog, null));
		builder.setTitle(R.string.dateTimeDialogTitle);
		
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	   DatePicker datePicker = (DatePicker)dateTimeDialog.findViewById(R.id.datePicker);
	        	   TimePicker timePicker = (TimePicker)dateTimeDialog.findViewById(R.id.timePicker);
	        	   
	        	   dateTimeEdit.setText(DateUtils.getDateTimeFromPickers(datePicker, timePicker));
	           }
	       });
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });
		
		dateTimeDialog = builder.create();
	}
	
	private Meeting createMeeting() {
		Meeting meeting = new Meeting();
		meeting.setTitle(titleEdit.getText().toString());
		meeting.setDescription(descriptionEdit.getText().toString());
		meeting.setDatetime(dateTimeEdit.getText().toString());
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
	
	private OnClickListener dateTimeChooseListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// display dialog
			dateTimeDialog.show();
	    }
	};
}
