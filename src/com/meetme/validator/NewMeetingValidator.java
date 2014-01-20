package com.meetme.validator;

import static com.meetme.store.MessageStore.EMPTY_DATETIME;
import static com.meetme.store.MessageStore.EMPTY_TITLE;
import static com.meetme.store.MessageStore.TITLE_TOO_LONG;
import static com.meetme.store.MessageStore.INVALID_DATETIME;
import android.content.Context;
import android.widget.EditText;


public class NewMeetingValidator extends Validator {
	
	private static final int TITLE_MAX_LENGTH = 255;
	private static final int DATETIME_LENGTH = 19;
	
	private EditText titleEdit;
	private EditText dateTimeEdit;
	
	/*
	 * Constructors
	 */
	public NewMeetingValidator(Context context, EditText titleEdit, EditText dateTimeEdit) {
		super(context);
		this.titleEdit = titleEdit;
		this.dateTimeEdit = dateTimeEdit;
	}
	
	/*
	 * private methods
	 */
	private boolean validateTitle() {
		boolean isTitleValid = true;
		String title = titleEdit.getText().toString();
		
		// Title must not be empty and not longer than 255 characters
		isTitleValid = (!title.isEmpty() && title.length() <= TITLE_MAX_LENGTH);
		
		if (title.isEmpty()) {
			isTitleValid = false;
			titleEdit.setError(getString(EMPTY_TITLE));
		}
		
		if (title.length() > TITLE_MAX_LENGTH) {
			isTitleValid = false;
			titleEdit.setError(getString(TITLE_TOO_LONG));
		}
		
		return isTitleValid;
	}
	
	private boolean validateDateTime() {
		boolean isDateTimeValid = true;
		String dateTime = dateTimeEdit.getText().toString();
		
		// Date must not be empty and well formated
		isDateTimeValid = !dateTime.isEmpty() && dateTime.length() <= DATETIME_LENGTH;
		
		if (dateTime.isEmpty()) {
			isDateTimeValid = false;
			dateTimeEdit.setError(getString(EMPTY_DATETIME));
		}
		
		if (dateTime.length() > DATETIME_LENGTH) {
			isDateTimeValid = false;
			dateTimeEdit.setError(getString(INVALID_DATETIME));
		}
		return isDateTimeValid;
	}
	
	/*
	 * Methods
	 */
	@Override
	public boolean validate() {
		return validateTitle()
				&& validateDateTime();
	}
}
