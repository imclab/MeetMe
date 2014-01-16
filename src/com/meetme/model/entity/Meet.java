package com.meetme.model.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Meet implements Serializable {
	
	static final long serialVersionUID = 500L;
	
	private int meetingId;
	private int userId;
	private String userEstimatedTime;
	private String userEstimatedDistance;
	private String userRefreshDateUpdated;
	private int userStatus;
	private String userStatusDateUpdated;
	private int userConfirmation;
	private String userConfirmationDateUpdated;
	private String userLatitudeLongitude;
	
	private static final String USER_ID = "user_id";
	private static final String USER_ETA = "user_eta";
	private static final String USER_EDA = "user_eda";
	private static final String USER_REFRESH_DATE_UPDATED = "user_refresh_date_updated";
	private static final String USER_STATUS = "user_status";
	private static final String USER_STATUS_DATE_UPDATED = "user_status_date_updated";
	private static final String USER_CONFIRMATION = "user_confirmation";
	private static final String USER_CONFIRMATION_DATE_UPDATED = "user_confirmation_date_updated";
	private static final String USER_LAT_LONG = "user_lat_long";
	
	private static String[] fieldNameArray = 
		{USER_ID, USER_ETA, USER_EDA, USER_REFRESH_DATE_UPDATED, USER_STATUS, USER_STATUS_DATE_UPDATED,
		USER_CONFIRMATION, USER_CONFIRMATION_DATE_UPDATED, USER_LAT_LONG};
	
	/*
	 * Constructors
	 */
	public Meet() {
	}
	
	public Meet(
			int meetingId,
			int userId, 
			String userEstimatedTime,
			String userEstimatedDistance, 
			String userRefreshDateUpdated,
			int userStatus, 
			String userStatusDateUpdated, 
			int userConfirmation,
			String userConfirmationDateUpdated, 
			String userLatitudeLongitude) {
		super();
		this.meetingId = meetingId;
		this.userId = userId;
		this.userEstimatedTime = userEstimatedTime;
		this.userEstimatedDistance = userEstimatedDistance;
		this.userRefreshDateUpdated = userRefreshDateUpdated;
		this.userStatus = userStatus;
		this.userStatusDateUpdated = userStatusDateUpdated;
		this.userConfirmation = userConfirmation;
		this.userConfirmationDateUpdated = userConfirmationDateUpdated;
		this.userLatitudeLongitude = userLatitudeLongitude;
	}



	/*
	 * Methods
	 */
	public static Meet getFromJSON(JSONObject meetJSON, int meetingId) {
		Meet meet = null;
		Map<String, String> fieldMap = new HashMap<String, String>();
		
		// Init map with 0 values
		for (String fieldName : fieldNameArray) {
				fieldMap.put(fieldName, "0");
		}
		
		for (String fieldName : fieldNameArray) {
			try {
				String fieldValue = meetJSON.get(fieldName).toString();
				fieldMap.put(fieldName, fieldValue);
			} catch (JSONException e) {
				Log.w(Meeting.class.getName(), "Could not parse entity field from JSON  : " + e.getMessage(), e);
			} catch (Exception e) {
				Log.e(Meeting.class.getName(), e.getMessage(), e);
			}
		}
		
		// Build meet object
		meet = new Meet(
				meetingId,
				Integer.parseInt(fieldMap.get(USER_ID)),
				fieldMap.get(USER_ETA), 
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
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Meet) ) {
			return false;
		} else {
			Meet other = (Meet)o;
			return (this.meetingId == other.meetingId) && (this.userId == other.userId);
		}
	}

	@Override
	public int hashCode() {
		return 4 * (meetingId + userId);
	}

	/*
	 * Accessors 
	 */
	public int getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(int meetingId) {
		this.meetingId = meetingId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserEstimatedTime() {
		return userEstimatedTime;
	}

	public void setUserEstimatedTime(String userEstimatedTime) {
		this.userEstimatedTime = userEstimatedTime;
	}

	public String getUserEstimatedDistance() {
		return userEstimatedDistance;
	}

	public void setUserEstimatedDistance(String userEstimatedDistance) {
		this.userEstimatedDistance = userEstimatedDistance;
	}

	public String getUserRefreshDateUpdated() {
		return userRefreshDateUpdated;
	}

	public void setUserRefreshDateUpdated(String userRefreshDateUpdated) {
		this.userRefreshDateUpdated = userRefreshDateUpdated;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserStatusDateUpdated() {
		return userStatusDateUpdated;
	}

	public void setUserStatusDateUpdated(String userStatusDateUpdated) {
		this.userStatusDateUpdated = userStatusDateUpdated;
	}

	public int getUserConfirmation() {
		return userConfirmation;
	}

	public void setUserConfirmation(int userConfirmation) {
		this.userConfirmation = userConfirmation;
	}

	public String getUserConfirmationDateUpdated() {
		return userConfirmationDateUpdated;
	}

	public void setUserConfirmationDateUpdated(String userConfirmationDateUpdated) {
		this.userConfirmationDateUpdated = userConfirmationDateUpdated;
	}

	public String getUserLatitudeLongitude() {
		return userLatitudeLongitude;
	}

	public void setUserLatitudeLongitude(String userLatitudeLongitude) {
		this.userLatitudeLongitude = userLatitudeLongitude;
	}
}
