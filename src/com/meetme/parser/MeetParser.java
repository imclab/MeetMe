package com.meetme.parser;

import java.util.Map;

import org.json.JSONObject;

import com.meetme.model.entity.Meet;

public class MeetParser extends AbstractParser<Meet> {
	
	private static final String USER_ID = "user_id";
	private static final String USER_ETA = "user_eta";
	private static final String USER_ETA_SECONDS = "user_eta_seconds";
	private static final String USER_EDA = "user_eda";
	private static final String USER_REFRESH_DATE_UPDATED = "user_refresh_date_updated";
	private static final String USER_STATUS = "user_status";
	private static final String USER_STATUS_DATE_UPDATED = "user_status_date_updated";
	private static final String USER_CONFIRMATION = "user_confirmation";
	private static final String USER_CONFIRMATION_DATE_UPDATED = "user_confirmation_date_updated";
	private static final String USER_LAT_LONG = "user_lat_long";
	
	private static String[] fieldNameArray = 
		{USER_ID, USER_ETA, USER_ETA_SECONDS, USER_EDA, USER_REFRESH_DATE_UPDATED, USER_STATUS, USER_STATUS_DATE_UPDATED,
		USER_CONFIRMATION, USER_CONFIRMATION_DATE_UPDATED, USER_LAT_LONG};
	
	/*
	 * Methods
	 */
	@Override
	public Meet getFromJSON(JSONObject meetJSON) {
		return null;
	}
	
	public Meet getFromJSON(JSONObject meetJSON, int meetingId) {
		Meet meet = null;

		Map<String, String> fieldMap = 
				super.getFieldMap(
						fieldNameArray, 
						meetJSON
					);
		
		// Build meet object
		meet = new Meet(
				meetingId,
				Integer.parseInt(fieldMap.get(USER_ID)),
				fieldMap.get(USER_ETA), 
				Long.parseLong(fieldMap.get(USER_ETA_SECONDS)),
				fieldMap.get(USER_EDA), 
				fieldMap.get(USER_REFRESH_DATE_UPDATED), 
				Integer.parseInt(fieldMap.get(USER_STATUS)), 
				fieldMap.get(USER_STATUS_DATE_UPDATED),
				Integer.parseInt(fieldMap.get(USER_CONFIRMATION)), 
				fieldMap.get(USER_CONFIRMATION_DATE_UPDATED), 
				fieldMap.get(USER_LAT_LONG)
			);
		
		return meet;
	}
}
