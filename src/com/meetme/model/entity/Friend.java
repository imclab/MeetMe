package com.meetme.model.entity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Friend {
	
	private int id;
	private String firstname;
	private String lastname;
	
	private static String[] fieldNameArray = 
		{"user_id", "firstname", "lastname"};
	
	/*
	 * Constructors
	 */
	public Friend() {
	}
	
	public Friend(
			int id, 
			String firstname, 
			String lastname) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	/*
	 * Methods
	 */
	public static Friend getFromJSON(JSONObject friendJSON) {
		Friend friend = null;
		Map<String, String> fieldMap = new HashMap<String, String>();
		
		// Init map with 0 values
		for (String fieldName : fieldNameArray) {
				fieldMap.put(fieldName, "0");
		}
		
		for (String fieldName : fieldNameArray) {
			try {
				String fieldValue = friendJSON.get(fieldName).toString();
				fieldMap.put(fieldName, fieldValue);
			} catch (JSONException e) {
				Log.w(Meeting.class.getName(), "Could not parse entity field from JSON  : " + e.getMessage());
			} catch (Exception e) {
				Log.e(Meeting.class.getName(), e.getMessage());
			}
		}
		
		// Build friend object
		friend = new Friend(
				Integer.parseInt(fieldMap.get("user_id")),
				fieldMap.get("firstname"),
				fieldMap.get("lastname")
			);
		
		return friend;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Friend) ) {
			return false;
		} else {
			Friend other = (Friend)o;
			return (this.id == other.id);
		}
	}

	@Override
	public int hashCode() {
		int hashCode = 4 * id + 3;
		return hashCode;
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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}
