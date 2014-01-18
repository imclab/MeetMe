package com.meetme.model.entity;

import java.io.Serializable;
import java.util.Map;

import org.json.JSONObject;

public class Friend extends AbstractEntity
	implements Comparable<Friend>, Serializable {
	
	static final long serialVersionUID = 200L;
	
	private int id;
	private String firstname;
	private String lastname;
	
	private static final String USER_ID = "user_id";
	private static final String FIRSTNAME = "firstname";
	private static final String LASTNAME = "lastname";
	
	private static String[] fieldNameArray = 
		{USER_ID, FIRSTNAME, LASTNAME};
	
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
		
		Map<String, String> fieldMap = 
				AbstractEntity.getFieldMap(
						fieldNameArray, 
						friendJSON, 
						Friend.class.getName()
					);
		
		// Build friend object
		friend = new Friend(
				Integer.parseInt(fieldMap.get(USER_ID)),
				fieldMap.get(FIRSTNAME),
				fieldMap.get(LASTNAME)
			);
		
		return friend;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Friend) ) {
			return false;
		} else {
			Friend other = (Friend)o;
			return this.id == other.id;
		}
	}

	@Override
	public int hashCode() {
		return 4 * id + 3;
	}
	
	@Override
	public int compareTo(Friend another) {
		return this.lastname.compareTo(another.lastname);
	}

	@Override
	public String toString() {
		return this.firstname + " " + this.lastname;
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
