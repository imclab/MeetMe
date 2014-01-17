package com.meetme.validator;

import static com.meetme.store.MessageStore.EMPTY_TITLE;
import static com.meetme.store.MessageStore.TITLE_TOO_LONG;
import android.content.Context;
import android.widget.EditText;


public class NewMeetingValidator extends Validator {
	
	private static final int TITLE_MAX_LENGTH = 255;
	
	private EditText titleEdit;
	
	/*
	 * Constructors
	 */
	public NewMeetingValidator(Context context, EditText titleEdit) {
		super(context);
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
		
		if (title.isEmpty()) {
			titleEdit.setError(getString(EMPTY_TITLE));
		}
		
		if (title.length() > TITLE_MAX_LENGTH) {
			titleEdit.setError(getString(TITLE_TOO_LONG));
		}
		
		return isTitleValid;
	}
	
	/*
	 * Methods
	 */
	@Override
	public boolean validate() {
		return validateTitle();
	}
}
