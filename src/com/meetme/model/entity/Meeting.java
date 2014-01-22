package com.meetme.model.entity;

import java.util.Set;
import java.util.TreeSet;

public class Meeting extends AbstractEntity
	implements Comparable<Meeting> {
	
	static final long serialVersionUID = 100L;
	
	private int id;
	private String title;
	private String description;
	private String dateTime;
	private long timestamp;
	private String locationGeo;
	private String locationText;
	private int hostUserId;

	private Set<Friend> friendSet;
	
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
			String dateTime,
			String locationGeo, 
			String locationText, 
			int hostUserId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.dateTime = dateTime;
		this.locationGeo = locationGeo;
		this.locationText = locationText;
		this.hostUserId = hostUserId;
		this.friendSet = new TreeSet<Friend>();
	}
	
	/*
	 * Methods 
	 */
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
		if (this.dateTime.equals(another.dateTime)) {
			return 1;
		}
		
		// Oldest to newest
		return this.dateTime.compareTo(another.dateTime);
	}
	
	@Override
	public String toString() {
		return this.id + ":" + this.title;
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
	
	public String getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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
}
