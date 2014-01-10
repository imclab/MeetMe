package com.meetme.core;

import java.util.Set;
import java.util.TreeSet;

import com.meetme.model.dao.FriendDao;
import com.meetme.model.dao.MeetingDao;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meeting;

public class SessionManager {
	
	private static SessionManager instance = null;
	private String userToken = null;
	private Set<Friend> friendSet = null;
	private Set<Meeting> meetingSet = null;
	
	private SessionManager() {
		this.friendSet = new TreeSet<Friend>();
		this.meetingSet = new TreeSet<Meeting>();
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
	
	public Set<Meeting> getMeetingSet() {
		return this.meetingSet;
	}
	
	/*
	 * Update methods
	 */
	public void addMeeting(Meeting newMeeting) {
		this.meetingSet.add(newMeeting);
	}
	
	public void updateFriendSet() {
		if (this.userToken != null && !this.userToken.isEmpty()) {
			this.friendSet.clear();
			this.friendSet.addAll(FriendDao.findFriendsOfUser(this.userToken));
		}
	}
	
	public void updateMeetingSet() {
		if (this.userToken != null && !this.userToken.isEmpty()) {
			this.meetingSet.clear();
			this.meetingSet.addAll(MeetingDao.findMeetingsOfUser(this.userToken));
		}
	}
}
