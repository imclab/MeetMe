package com.meetme.model.entity.notification;

import java.io.Serializable;
import java.util.Map;

import org.json.JSONObject;

import com.meetme.model.entity.AbstractEntity;

public class FriendInviteNotification extends AbstractEntity
	implements Comparable<FriendInviteNotification>, Serializable {
	
	static final long serialVersionUID = 13L;
	
	private int inviterId;
	private String inviterFirstname;
	private String inviterLastname;
	private String dateTime;
	
	private static final String INVITER_ID = "friend_id";
	private static final String INVITER_FIRSTNAME = "firstname";
	private static final String INVITER_LASTNAME = "lastname";
	private static final String DATETIME = "date_created";
	
	private static String[] fieldNameArray = 
		{INVITER_ID, INVITER_FIRSTNAME, INVITER_LASTNAME, DATETIME};

	/*
	 * Constructors
	 */
	public FriendInviteNotification() {
	}
	
	public FriendInviteNotification(
			int inviterId, 
			String inviterFirstname,
			String inviterLastname, 
			String dateTime) {
		super();
		this.inviterId = inviterId;
		this.inviterFirstname = inviterFirstname;
		this.inviterLastname = inviterLastname;
		this.dateTime = dateTime;
	}
	
	/*
	 * Methods
	 */
	public static FriendInviteNotification getFromJSON(JSONObject notificationJSON) {
		FriendInviteNotification notification = null;
		
		Map<String, String> fieldMap = 
				AbstractEntity.getFieldMap(
						fieldNameArray, 
						notificationJSON, 
						FriendInviteNotification.class.getName()
					);
		
		// Build friend invite notification object
		notification = new FriendInviteNotification(
				Integer.parseInt(fieldMap.get(INVITER_ID)),
				fieldMap.get(INVITER_FIRSTNAME),
				fieldMap.get(INVITER_LASTNAME),
				fieldMap.get(DATETIME)
			);
		
		return notification;
	}

	/*
	 * Two friend invite notifications are equal only if
	 * - they come from the same friend 
	 * and 
	 * - they are created at the same date and time
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FriendInviteNotification) ) {
			return false;
		} else {
			FriendInviteNotification other = (FriendInviteNotification)o;
			return (this.inviterId == other.inviterId) 
					&& (this.dateTime.equals(other.dateTime));
		}
	}

	@Override
	public int hashCode() {
		return 4 * this.inviterId + dateTime.hashCode();
	}

	@Override
	public int compareTo(FriendInviteNotification another) {
		// Newest to oldest
		return -1 * this.dateTime.compareTo(another.dateTime);
	}
	
	/*
	 * Accessors
	 */
	public int getInviterId() {
		return inviterId;
	}

	public void setInviterId(int inviterId) {
		this.inviterId = inviterId;
	}

	public String getInviterFirstname() {
		return inviterFirstname;
	}

	public void setInviterFirstname(String inviterFirstname) {
		this.inviterFirstname = inviterFirstname;
	}

	public String getInviterLastname() {
		return inviterLastname;
	}

	public void setInviterLastname(String inviterLastname) {
		this.inviterLastname = inviterLastname;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
