package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.MEETING_OPERATION;
import static com.meetme.store.ServerParameterStore.MEETING_OPERATION_NOTIFICATION;
import static com.meetme.store.ServerParameterStore.MEETING_TOKEN;
import static com.meetme.store.ServerUrlStore.MEETING_URL;

import java.util.Set;

import org.json.JSONObject;

import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.MeetingInviteNotification;
import com.meetme.parser.MeetingInviteNotificationParser;

public class MeetingInviteNotificationDao extends AbstractDao<MeetingInviteNotification> {
	
	public MeetingInviteNotificationDao() {
		super(new MeetingInviteNotificationParser());
	}
	
	public Set<MeetingInviteNotification> findMeetingInviteNotifications(String userToken) {
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(MEETING_OPERATION, MEETING_OPERATION_NOTIFICATION);
		parameters.put(MEETING_TOKEN, userToken);
		
		// Send request
		responseJSON = HttpUtils.post(MEETING_URL, parameters);
		
		// Built notification set from JSON response
		return super.findAllFromUser(responseJSON, userToken);
	}
}
