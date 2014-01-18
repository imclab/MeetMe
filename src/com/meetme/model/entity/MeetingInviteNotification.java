package com.meetme.model.entity;

import java.io.Serializable;


public class MeetingInviteNotification extends AbstractEntity
	implements Comparable<MeetingInviteNotification>, Serializable {
	
	static final long serialVersionUID = 13L;
	
	private int meetingId;
	private String meetingTitle;
	private String meetingDescription;
	private String meetingDateTime;
	private String meetingLocationText;
	private int meetingHostUserId;
	private String dateTime;
	
	/*
	 * Constructors
	 */
	public MeetingInviteNotification() {
	}
	
	public MeetingInviteNotification(
			int meetingId, 
			String meetingTitle,
			String meetingDescription, 
			String meetingDateTime,
			String meetingLocationText, 
			int meetingHostUserId,
			String dateTime) {
		super();
		this.meetingId = meetingId;
		this.meetingTitle = meetingTitle;
		this.meetingDescription = meetingDescription;
		this.meetingDateTime = meetingDateTime;
		this.meetingLocationText = meetingLocationText;
		this.meetingHostUserId = meetingHostUserId;
		this.dateTime = dateTime;
	}

	/*
	 * Methods
	 */

	/*
	 * Two meeting invite notifications are equal only if
	 * - they concern the same meeting
	 * - they are sent at the same date and time
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MeetingInviteNotification) ) {
			return false;
		} else {
			MeetingInviteNotification other = (MeetingInviteNotification)o;
			return (this.meetingId == other.meetingId) 
					&& (this.dateTime.equals(other.dateTime));
		}
	}

	@Override
	public int hashCode() {
		return 4 * this.meetingId + dateTime.hashCode() - 3;
	}

	@Override
	public int compareTo(MeetingInviteNotification another) {
		if (this.dateTime.equals(another.dateTime)) {
			return -1;
		}
		
		// Newest to oldest
		return -1 * this.dateTime.compareTo(another.dateTime);
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

	public String getMeetingTitle() {
		return meetingTitle;
	}

	public void setMeetingTitle(String meetingTitle) {
		this.meetingTitle = meetingTitle;
	}

	public String getMeetingDescription() {
		return meetingDescription;
	}

	public void setMeetingDescription(String meetingDescription) {
		this.meetingDescription = meetingDescription;
	}

	public String getMeetingDateTime() {
		return meetingDateTime;
	}

	public void setMeetingDateTime(String meetingDateTime) {
		this.meetingDateTime = meetingDateTime;
	}

	public String getMeetingLocationText() {
		return meetingLocationText;
	}

	public void setMeetingLocationText(String meetingLocationText) {
		this.meetingLocationText = meetingLocationText;
	}

	public int getMeetingHostUserId() {
		return meetingHostUserId;
	}

	public void setMeetingHostUserId(int meetingHostUserId) {
		this.meetingHostUserId = meetingHostUserId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
