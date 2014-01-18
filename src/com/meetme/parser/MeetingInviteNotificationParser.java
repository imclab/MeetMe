package com.meetme.parser;

import java.util.Map;

import org.json.JSONObject;

import com.meetme.model.entity.MeetingInviteNotification;

public class MeetingInviteNotificationParser 
	extends AbstractParser<MeetingInviteNotification> {
	private static final String JSON_KEY_FOR_FIND_ALL_FROM_USER = "meetings";
	
	private static final String MEETING_ID = "meeting_id";
	private static final String MEETING_TITLE = "title";
	private static final String MEETING_DESCRIPTION = "description";
	private static final String MEETING_DATETIME = "datetime";
	private static final String MEETING_LOCATION_TEXT = "location_text";
	private static final String MEETING_HOST_USER_ID = "host_user_id";
	private static final String DATETIME = "notification_date_created";
	
	private static String[] fieldNameArray = 
		{MEETING_ID, MEETING_TITLE, MEETING_DESCRIPTION, MEETING_DATETIME, 
		MEETING_LOCATION_TEXT, MEETING_HOST_USER_ID, DATETIME};
	
	/*
	 * Constructor
	 */
	public MeetingInviteNotificationParser() {
		super.setJSONKeyForFindAllFromUser(JSON_KEY_FOR_FIND_ALL_FROM_USER);
	}
	
	/*
	 * Methods
	 */
	@Override
	public MeetingInviteNotification getFromJSON(JSONObject notificationJSON) {
		MeetingInviteNotification notification = null;
		
		Map<String, String> fieldMap = 
				super.getFieldMap(
						fieldNameArray, 
						notificationJSON
					);
		
		// Build friend invite notification object
		notification = new MeetingInviteNotification(
				Integer.parseInt(fieldMap.get(MEETING_ID)),
				fieldMap.get(MEETING_TITLE),
				fieldMap.get(MEETING_DESCRIPTION),
				fieldMap.get(MEETING_DATETIME),
				fieldMap.get(MEETING_LOCATION_TEXT),
				Integer.parseInt(fieldMap.get(MEETING_HOST_USER_ID)),
				fieldMap.get(DATETIME)
			);
		
		return notification;
	}
}
