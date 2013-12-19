package com.meetme.validator;

import android.widget.EditText;
import static com.meetme.protocol.store.MessageStore.*;


public class NewMeetingValidator {
	
	private static final int TITLE_MAX_LENGTH = 255;
	
	private EditText titleEdit;
	
	/*
	 * Constructors
	 */
	public NewMeetingValidator(EditText titleEdit) {
		super();
		this.titleEdit = titleEdit;
	}
	
	/*
	 * private methods
	 */
	private boolean validateTitle() {
		boolean isTitleValid = false;
		String title = titleEdit.getText().toString();
		
		// Title must not be empty and not longer than 255 characters
		isTitleValid = (!title.isEmpty() && title.length() <= TITLE_MAX_LENGTH);
		
		if (!isTitleValid) {
			titleEdit.setError(TITLE_TOO_LONG);
		}
		
		return isTitleValid;
	}
	
	/*
	 * Methods
	 */
	public boolean validate() {
		return (validateTitle());
	}
}
