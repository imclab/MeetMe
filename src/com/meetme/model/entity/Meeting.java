package com.meetme.model.entity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Meeting {
	
	private int id;
	private String title;
	private String description;
	private String datetime;
	private String locationGeo;
	private String locationText;
	private int hostUserId;
	
	private static String[] fieldNameArray = 
		{"meeting_id", "title", "description", "datetime", "location_geo", "location_text", "host_user_id"};

	/*
	 * Constructors
	 */
	public Meeting() {	
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
	}
	
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
				Log.w(Meeting.class.getName(), "Could not parse entity field from JSON  : " + e.getMessage());
			} catch (Exception e) {
				Log.e(Meeting.class.getName(), e.getMessage());
			}
		}
		
		// Build meeting object
		meeting = new Meeting(
				Integer.parseInt(fieldMap.get("meeting_id")),
				fieldMap.get("title"),
				fieldMap.get("description"),
				fieldMap.get("datetime"),
				fieldMap.get("location_geo"),
				fieldMap.get("location_text"),
				Integer.parseInt(fieldMap.get("host_user_id"))
			);
		
		return meeting;
	}
	
	/*
	 * Methods 
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Meeting) ) {
			return false;
		} else {
			Meeting other = (Meeting)o;
			return (this.id == other.id);
		}
	}

	@Override
	public int hashCode() {
		int hashCode = 2 * id + 5;
		return hashCode;
	}

	/*
	 * Accessors
	 */
	public int getId() {
		return id;
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
}
