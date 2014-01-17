package com.meetme.validator;

import static com.meetme.store.MessageStore.EMPTY_LOCATION;
import android.content.Context;
import android.widget.EditText;

public class PickLocationValidator extends Validator {
	
	private EditText findLocationEdit;

	/*
	 * Constructors
	 */
	public PickLocationValidator(Context context, EditText findLocationEdit) {
		super(context);
		this.findLocationEdit = findLocationEdit;
	}
	
	/*
	 * private methods
	 */
	private boolean validateFindLocation() {
		boolean isFindLocationValid = false;
		String findLocation = findLocationEdit.getText().toString();
		
		// Location must not be empty
		isFindLocationValid = !findLocation.isEmpty();
		
		if(!isFindLocationValid) {
			findLocationEdit.setError(getString(EMPTY_LOCATION));
        }
		
		return isFindLocationValid;
	}
	
	/*
	 * Methods
	 */
	@Override
	public boolean validate() {
		return validateFindLocation();
	}
}
