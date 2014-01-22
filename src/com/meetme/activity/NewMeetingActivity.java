package com.meetme.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.meetme.R;
import com.meetme.core.DateUtils;
import com.meetme.core.SessionManager;
import com.meetme.model.entity.Meeting;
import com.meetme.validator.NewMeetingValidator;

public class NewMeetingActivity extends Activity {

	private EditText titleEdit;
	private EditText descriptionEdit;
	private EditText dateTimeEdit;
	private long timestamp;
	private Button pickLocationButton;
	private Button dateTimeChooseButton;
	AlertDialog dateTimeDialog;
	private NewMeetingValidator newMeetingValidator;
	
	private static final String NEW_MEETING_INTENT_KEY = "newMeeting";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_meeting);
		
		titleEdit = (EditText)findViewById(R.id.titleEdit);
		descriptionEdit = (EditText)findViewById(R.id.descriptionEdit);
		dateTimeEdit = (EditText)findViewById(R.id.dateTimeEdit);
		pickLocationButton = (Button)findViewById(R.id.pickLocationButton);
		pickLocationButton.setOnClickListener(pickLocationListener);
		dateTimeChooseButton = (Button)findViewById(R.id.dateTimeChooseButton);
		dateTimeChooseButton.setOnClickListener(dateTimeChooseListener);
		
		initDateTimeDialog();
		
		newMeetingValidator = new NewMeetingValidator(
				getApplicationContext(), 
				titleEdit,
				dateTimeEdit
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
	        	   timestamp = DateUtils.getTimestampFromPickers(datePicker, timePicker);
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
		meeting.setHostUserId(SessionManager.getInstance().getUser().getId());
		meeting.setTitle(titleEdit.getText().toString());
		meeting.setDescription(descriptionEdit.getText().toString());
		meeting.setDateTime(dateTimeEdit.getText().toString());
		meeting.setTimestamp(timestamp);
		
		return meeting;
	}
	
	/*
	 * Listeners
	 */
	private OnClickListener pickLocationListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (newMeetingValidator.validate()) {
				// Start pick location activity
				Intent intent = new Intent(NewMeetingActivity.this, PickLocationActivity.class);
				intent.putExtra(NEW_MEETING_INTENT_KEY, createMeeting());
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
