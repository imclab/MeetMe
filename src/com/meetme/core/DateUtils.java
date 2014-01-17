package com.meetme.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.widget.DatePicker;
import android.widget.TimePicker;

public abstract class DateUtils {
	
	private DateUtils() {
	}
	
	/*
	 * Private methods
	 */
	private static Date getDateObject(DatePicker datePicker, TimePicker timePicker) {
		Calendar calendar = new GregorianCalendar(
				datePicker.getYear(), 
				datePicker.getMonth(), 
				datePicker.getDayOfMonth(), 
				timePicker.getCurrentHour(), 
				timePicker.getCurrentMinute()
			);
		
		return calendar.getTime(); 
	}
	
	/*
	 * Methods
	 */
	public static String getDateTimeFromPickers(DatePicker datePicker, TimePicker timePicker) {
		Date date = getDateObject(datePicker, timePicker);
		
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static long getTimestampFromPickers(DatePicker datePicker, TimePicker timePicker) {
		Date date = getDateObject(datePicker, timePicker);
		
		// Date.getTime() returns milliseconds : unix timestamp * 1000
		// Thus, we divide per 1000 to get a unix timestamp
		return date.getTime() / 1000;
	}
}
