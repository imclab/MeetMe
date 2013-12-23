package com.meetme.core;

import java.util.Set;
import java.util.TreeSet;

import com.meetme.model.entity.Friend;

public class SessionManager {
	
	private static SessionManager instance = null;
	private String userToken;
	private Set<Friend> friendSet = null;
	
	private SessionManager() {
		this.friendSet = new TreeSet<Friend>();
	}
	
	/*
	 * Accessors
	 */
	public static SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
		}
		
		return instance;
	}
	
	public String getUserToken() {
		return this.userToken;
	}
	
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
	public Set<Friend> getFriendSet() {
		return this.friendSet;
	}
	
	/*
	 * Update methods
	 */
	public void updateFriendSet() {
		
	}
	
	public void updateMeetingSet() {
		
	}
}
