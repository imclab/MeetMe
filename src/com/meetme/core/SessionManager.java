package com.meetme.core;

import java.util.Set;
import java.util.TreeSet;

import com.meetme.model.dao.FriendDao;
import com.meetme.model.dao.FriendInviteNotificationDao;
import com.meetme.model.dao.MeetingDao;
import com.meetme.model.dao.MeetingInviteNotificationDao;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.FriendInviteNotification;
import com.meetme.model.entity.Meeting;
import com.meetme.model.entity.MeetingInviteNotification;

public class SessionManager {
	
	private static SessionManager instance = null;
	private String email = null;
	private String userToken = null;
	private Set<Friend> friendSet = null;
	private Set<Meeting> meetingSet = null;
	private Set<FriendInviteNotification> friendNotificationSet = null;
	private Set<MeetingInviteNotification> meetingNotificationSet = null;

	private FriendDao friendDao;
	private MeetingDao meetingDao;
	private FriendInviteNotificationDao friendNotificationDao;
	private MeetingInviteNotificationDao meetingNotificationDao;
	
	private SessionManager() {
		this.friendSet = new TreeSet<Friend>();
		this.meetingSet = new TreeSet<Meeting>();
		this.friendNotificationSet = new TreeSet<FriendInviteNotification>();
		this.meetingNotificationSet = new TreeSet<MeetingInviteNotification>();
		this.friendDao = new FriendDao();
		this.meetingDao = new MeetingDao();
		this.friendNotificationDao = new FriendInviteNotificationDao();
		this.meetingNotificationDao = new MeetingInviteNotificationDao();
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
	
	public Set<FriendInviteNotification> getFriendNotificationSet() {
		return this.friendNotificationSet;
	}
	
	public Set<MeetingInviteNotification> getMeetingNotificationSet() {
		return this.meetingNotificationSet;
	}
	
	public void setFriendSet(Set<Friend> friendSet) {
		this.friendSet = friendSet;
	}

	public void setMeetingSet(Set<Meeting> meetingSet) {
		this.meetingSet = meetingSet;
	}

	public void setFriendNotificationSet(
			Set<FriendInviteNotification> friendNotificationSet) {
		this.friendNotificationSet = friendNotificationSet;
	}

	public void setMeetingNotificationSet(
			Set<MeetingInviteNotification> meetingNotificationSet) {
		this.meetingNotificationSet = meetingNotificationSet;
	}

	/**
	 * Check if the session is valid by checking token validity
	 */
	private boolean isSessionValid() {
		return this.userToken != null && !this.userToken.isEmpty();
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
	
	public void update() {
		updateFriendSet();
		updateMeetingSet();
		updateFriendgNotificationSet();
		updateMeetingNotificationSet();
	}
	
	public void updateFriendSet() {
		if (isSessionValid()) {
			this.friendSet.clear();
			this.friendSet.addAll(this.friendDao.findFriendsOfUser(this.userToken));
		}
	}
	
	public void updateMeetingSet() {
		if (isSessionValid()) {
			this.meetingSet.clear();
			this.meetingSet.addAll(this.meetingDao.findMeetingsOfUser(this.userToken));
		}
	}
	
	public void updateFriendgNotificationSet() {
		if (isSessionValid()) {
			this.friendNotificationSet.clear();
			this.friendNotificationSet.addAll(this.friendNotificationDao.findFriendInviteNotifications(this.userToken));
		}
	}
	
	public void updateMeetingNotificationSet() {
		if (isSessionValid()) {
			this.meetingNotificationSet.clear();
			this.meetingNotificationSet.addAll(this.meetingNotificationDao.findMeetingInviteNotifications(this.userToken));
		}
	}
	
	public void updateMeeting(int meetingId) {
		if (isSessionValid()) {
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
