package com.meetme.model.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Meeting implements Comparable<Meeting> {
	
	private int id;
	private String title;
	private String description;
	private String datetime;
	private String locationGeo;
	private String locationText;
	private int hostUserId;

	private Set<Friend> friendSet;
	
	private static final String MEETING_ID = "meeting_id";
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String DATETIME = "datetime";
	private static final String LOCATION_GEO = "location_geo";
	private static final String LOCATION_TEXT = "location_text";
	private static final String HOST_USER_ID = "host_user_id";
	
	private static String[] fieldNameArray = 
		{MEETING_ID, TITLE, DESCRIPTION, DATETIME, LOCATION_GEO, LOCATION_TEXT, HOST_USER_ID};

	/*
	 * Constructors
	 */
	public Meeting() {	
		this.friendSet = new TreeSet<Friend>();
	}
	
	public Meeting(
			int id, 
			String title, 
			String description, 
			String datetime,
			String locationGeo, 
			String locationText, 
			int hostUserId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.datetime = datetime;
		this.locationGeo = locationGeo;
		this.locationText = locationText;
		this.hostUserId = hostUserId;
		this.friendSet = new TreeSet<Friend>();
	}
	
	/*
	 * Methods 
	 */
	public static Meeting getFromJSON(JSONObject meetingJSON) {
		Meeting meeting = null;
		Map<String, String> fieldMap = new HashMap<String, String>();
		
		// Init map with 0 values
		for (String fieldName : fieldNameArray) {
				fieldMap.put(fieldName, "0");
		}
		
		for (String fieldName : fieldNameArray) {
			try {
				String fieldValue = meetingJSON.get(fieldName).toString();
				fieldMap.put(fieldName, fieldValue);
			} catch (JSONException e) {
				Log.w(Meeting.class.getName(), "Could not parse entity field from JSON  : " + e.getMessage(), e);
			} catch (Exception e) {
				Log.e(Meeting.class.getName(), e.getMessage(), e);
			}
		}
		
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
		
		return meeting;
	}
	
	public void addFriend(Friend friend) {
		this.friendSet.add(friend);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Meeting) ) {
			return false;
		} else {
			Meeting other = (Meeting)o;
			return this.id == other.id;
		}
	}

	@Override
	public int hashCode() {
		return 2 * id + 5;
	}

	@Override
	public int compareTo(Meeting another) {
		return this.datetime.compareTo(another.datetime);
	}

	/*
	 * Accessors
	 */
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDatetime() {
		return datetime;
	}
	
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	public String getLocationGeo() {
		return locationGeo;
	}
	
	public void setLocationGeo(String locationGeo) {
		this.locationGeo = locationGeo;
	}
	
	public String getLocationText() {
		return locationText;
	}
	
	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}
	
	public int getHostUserId() {
		return hostUserId;
	}
	
	public void setHostUserId(int hostUserId) {
		this.hostUserId = hostUserId;
	}

	public Set<Friend> getFriendSet() {
		return friendSet;
	}

	public void setFriendSet(Set<Friend> friendSet) {
		this.friendSet = friendSet;
	}
}
