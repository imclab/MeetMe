package com.meetme.core;

import java.util.Set;
import java.util.TreeSet;

import com.meetme.model.dao.FriendDao;
import com.meetme.model.dao.MeetingDao;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meeting;

public class SessionManager {
	
	private static SessionManager instance = null;
	private String email = null;
	private String userToken = null;
	private Set<Friend> friendSet = null;
	private Set<Meeting> meetingSet = null;

	private FriendDao friendDao;
	private MeetingDao meetingDao;
	
	private SessionManager() {
		this.friendSet = new TreeSet<Friend>();
		this.meetingSet = new TreeSet<Meeting>();
		this.friendDao = new FriendDao();
		this.meetingDao = new MeetingDao();
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
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
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
	 * Find methods
	 */
	public Friend getFriendById(int friendId) {
		for (Friend friend : this.friendSet) {
			if (friend.getId() == friendId) {
				return friend;
			}
		}
		
		return null;
	}
	
	public Meeting getMeetingById(int meetingId) {
		for (Meeting meeting : this.meetingSet) {
			if (meeting.getId() == meetingId) {
				return meeting;
			}
		}
		
		return null;
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
			this.friendSet.addAll(this.friendDao.findFriendsOfUser(this.userToken));
		}
	}
	
	public void updateMeetingSet() {
		if (this.userToken != null && !this.userToken.isEmpty()) {
			this.meetingSet.clear();
			this.meetingSet.addAll(this.meetingDao.findMeetingsOfUser(this.userToken));
		}
	}
	
	public void updateMeeting(int meetingId) {
		if (this.userToken != null && !this.userToken.isEmpty()) {
			for (Meeting meeting : this.meetingSet) {
				if (meeting.getId() == meetingId) {
					Meeting updatedMeeting = this.meetingDao.findMeetingById(meetingId, this.userToken);
					this.meetingSet.remove(meeting);
					this.meetingSet.add(updatedMeeting);
				}
			}
		}
	}
}
