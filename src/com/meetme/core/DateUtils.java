package com.meetme.core;

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
 	    
 	    
 	    int hour = timePicker.getCurrentHour();
 	    
 	    if (hour < 10) {
			dateTime.append("0");
		}
 	    
 	    dateTime.append(hour).append(":");
 	    
 	    int minute = timePicker.getCurrentMinute();
 	    
 	    if (minute < 10) {
			dateTime.append("0");
 	    }
 	   
 	    dateTime.append(minute);
		
		return dateTime.toString();
	}
}
