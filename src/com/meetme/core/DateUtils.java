package com.meetme.core;

import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

public abstract class DateUtils {
	
	private DateUtils() {
	}
	
	/*
	 * Methods
	 */
	public static String getDateTimeFromPickers(DatePicker datePicker, TimePicker timePicker) {
		StringBuilder dateTime = new StringBuilder();
		
		int month = datePicker.getMonth();
		month++;
		
		dateTime.append(datePicker.getDayOfMonth()).append("/");
		
		if (month < 10) {
			dateTime.append("0");
		}
		
		dateTime.append(month).append("/");
 	    dateTime.append(datePicker.getYear()).append(" ");
 	    dateTime.append(timePicker.getCurrentHour()).append(":");
 	    dateTime.append(timePicker.getCurrentMinute());
		
		return dateTime.toString();
	}
}
