package com.meetme.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.widget.DatePicker;
import android.widget.TimePicker;

@SuppressLint("SimpleDateFormat")
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
	public static Calendar getCalendarFromDateTime(String dateTime) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(
				Integer.parseInt(dateTime.substring(0, 4)), 
				Integer.parseInt(dateTime.substring(5, 7)) - 1, 
				Integer.parseInt(dateTime.substring(8, 10)), 
				Integer.parseInt(dateTime.substring(11, 13)), 
				Integer.parseInt(dateTime.substring(14, 16)), 
				Integer.parseInt(dateTime.substring(17, 19))
			);
		
		return calendar;
	}
	
	public static String formatDateTime(String dateTime) {
		Calendar calendar = getCalendarFromDateTime(dateTime);
        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy '-' HH:mm");
        return dateFormat.format(calendar.getTime());
	}
	
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
