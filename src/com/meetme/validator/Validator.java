package com.meetme.validator;

import android.content.Context;

public abstract class Validator {
	
	/*
	 * Used to retrieve String resources for error messages
	 */
	protected Context context;
	
	/*
	 * Constructor
	 */
	protected Validator(Context context) {
		this.context = context;
	}
	
	/*
	 * Shortcut for context.getString()
	 */
	protected String getString(int resId) {
		return context.getString(resId);
	}
	
	public abstract boolean validate();
}
