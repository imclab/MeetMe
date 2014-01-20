package com.meetme.parser;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.model.entity.Meeting;

public class MeetingParser extends AbstractParser<Meeting> {

	private static final String MEETING_ID = "meeting_id";
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String DATETIME = "datetime";
	private static final String LOCATION_GEO = "location_geo";
	private static final String LOCATION_TEXT = "location_text";
	private static final String HOST_USER_ID = "host_user_id";
	private static final String USERS = "users";
	
	private static String[] fieldNameArray = 
		{MEETING_ID, TITLE, DESCRIPTION, DATETIME, LOCATION_GEO, LOCATION_TEXT, HOST_USER_ID};
	
	FriendParser friendEntityParser = null;
	
	public MeetingParser() {
		this.friendEntityParser = new FriendParser();
	}
	
	/*
	 * Methods
	 */
	@Override
	public Meeting getFromJSON(JSONObject meetingJSON) {
		Meeting meeting = null;
		
		Map<String, String> fieldMap = 
				super.getFieldMap(
						fieldNameArray, 
						meetingJSON
					);
		
		// Build meeting object
		meeting = new Meeting(
				Integer.parseInt(fieldMap.get(MEETING_ID)),
				fieldMap.get(TITLE),
				fieldMap.get(DESCRIPTION),
				fieldMap.get(DATETIME),
				fieldMap.get(LOCATION_GEO),
				fieldMap.get(LOCATION_TEXT),
				Integer.parseInt(fieldMap.get(HOST_USER_ID))
			);
		
		// Parse users
		try {
			JSONArray userArray = (JSONArray)meetingJSON.get(USERS);
			int userArrayLength = userArray.length();
			
			for (int i = 0; i < userArrayLength; i++) {
				meeting.addFriend(friendEntityParser.getFromJSON(userArray.getJSONObject(i)));
			}
			
		} catch (JSONException e) {
			Log.w(MeetingParser.class.getName(), COULD_NOT_PARSE_FIELD_FROM_JSON + e.getMessage(), e);
		} catch (Exception e) {
			Log.e(MeetingParser.class.getName(), e.getMessage(), e);
		}
		
		return meeting;
	}
}
