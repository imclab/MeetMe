package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.MEETING_OPERATION;
import static com.meetme.store.ServerParameterStore.MEETING_OPERATION_NOTIFICATION;
import static com.meetme.store.ServerParameterStore.MEETING_TOKEN;
import static com.meetme.store.ServerUrlStore.MEETING_URL;

import java.util.Set;

import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.MeetingInviteNotification;
import com.meetme.parser.MeetingInviteNotificationParser;

public class MeetingInviteNotificationDao extends AbstractDao<MeetingInviteNotification> {
	private static final String JSON_KEY_FOR_FIND_MEETING_INVITE_NOTIFICATIONS = "meetings";
	
	public MeetingInviteNotificationDao() {
		super(new MeetingInviteNotificationParser());
	}
	
	public Set<MeetingInviteNotification> findMeetingInviteNotifications(String userToken) {
		// Add parameters
		HttpParameters parameters = new HttpParameters();
		parameters.put(MEETING_OPERATION, MEETING_OPERATION_NOTIFICATION);
		parameters.put(MEETING_TOKEN, userToken);
		
		// Built notification set from JSON response
		return super.findAllFromUser(
				HttpUtils.post(MEETING_URL, parameters), 
				JSON_KEY_FOR_FIND_MEETING_INVITE_NOTIFICATIONS
			);
	}
}
