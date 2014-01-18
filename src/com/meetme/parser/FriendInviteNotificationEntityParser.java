package com.meetme.parser;

import java.util.Map;

import org.json.JSONObject;

import com.meetme.model.entity.FriendInviteNotification;

public class FriendInviteNotificationEntityParser 
	extends AbstractEntityParser<FriendInviteNotification> {
	
	private static final String INVITER_ID = "friend_id";
	private static final String INVITER_FIRSTNAME = "firstname";
	private static final String INVITER_LASTNAME = "lastname";
	private static final String DATETIME = "date_created";
	
	private static String[] fieldNameArray = 
		{INVITER_ID, INVITER_FIRSTNAME, INVITER_LASTNAME, DATETIME};
	
	@Override
	public FriendInviteNotification getFromJSON(JSONObject notificationJSON) {
		FriendInviteNotification notification = null;
		
		Map<String, String> fieldMap = 
				super.getFieldMap(
						fieldNameArray, 
						notificationJSON
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
}
