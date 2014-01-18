package com.meetme.model.entity.notification;

import java.io.Serializable;
import java.util.Map;

import org.json.JSONObject;

import com.meetme.model.entity.AbstractEntity;

public class MeetingInviteNotification extends AbstractEntity
	implements Comparable<MeetingInviteNotification>, Serializable {
	
	static final long serialVersionUID = 13L;
	
	private int meetingId;
	private String meetingTitle;
	private String meetingDescription;
	private String meetingDateTime;
	private String meetingLocationText;
	private int meetingHostUserId;
	private int inviterId;
	private String dateTime;
	
	private static final String MEETING_ID = "meeting_id";
	private static final String MEETING_TITLE = "title";
	private static final String MEETING_DESCRIPTION = "description";
	private static final String MEETING_DATETIME = "datetime";
	private static final String MEETING_LOCATION_TEXT = "location_text";
	private static final String MEETING_HOST_USER_ID = "host_user_id";
	private static final String INVITER_ID = "user_id";
	private static final String DATETIME = "date_created";
	
	private static String[] fieldNameArray = 
		{MEETING_ID, MEETING_TITLE, MEETING_DESCRIPTION, MEETING_DATETIME, 
		MEETING_LOCATION_TEXT, MEETING_HOST_USER_ID, INVITER_ID, DATETIME};

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
			int inviterId,
			String dateTime) {
		super();
		this.meetingId = meetingId;
		this.meetingTitle = meetingTitle;
		this.meetingDescription = meetingDescription;
		this.meetingDateTime = meetingDateTime;
		this.meetingLocationText = meetingLocationText;
		this.meetingHostUserId = meetingHostUserId;
		this.inviterId = inviterId;
		this.dateTime = dateTime;
	}

	/*
	 * Methods
	 */
	public static MeetingInviteNotification getFromJSON(JSONObject notificationJSON) {
		MeetingInviteNotification notification = null;
		
		Map<String, String> fieldMap = 
				AbstractEntity.getFieldMap(
						fieldNameArray, 
						notificationJSON, 
						MeetingInviteNotification.class.getName()
					);
		
		// Build friend invite notification object
		notification = new MeetingInviteNotification(
				Integer.parseInt(fieldMap.get(MEETING_ID)),
				fieldMap.get(MEETING_TITLE),
				fieldMap.get(MEETING_DESCRIPTION),
				fieldMap.get(MEETING_DATETIME),
				fieldMap.get(MEETING_LOCATION_TEXT),
				Integer.parseInt(fieldMap.get(MEETING_HOST_USER_ID)),
				Integer.parseInt(fieldMap.get(INVITER_ID)),
				fieldMap.get(DATETIME)
			);
		
		return notification;
	}

	/*
	 * Two meeting invite notifications are equal only if
	 * - they concern the same meeting
	 * and
	 * - they are sent by the same friend
	 * and
	 * - they are sent at the same date and time
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MeetingInviteNotification) ) {
			return false;
		} else {
			MeetingInviteNotification other = (MeetingInviteNotification)o;
			return (this.meetingId == other.meetingId) 
					&& this.inviterId == other.inviterId
					&& (this.dateTime.equals(other.dateTime));
		}
	}

	@Override
	public int hashCode() {
		return 4 * this.meetingId + dateTime.hashCode() - 3;
	}

	@Override
	public int compareTo(MeetingInviteNotification another) {
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

	public int getInviterId() {
		return inviterId;
	}

	public void setInviterId(int inviterId) {
		this.inviterId = inviterId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
