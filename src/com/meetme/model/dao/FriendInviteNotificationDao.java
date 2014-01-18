package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION;
import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION_NOTIFICATION;
import static com.meetme.store.ServerParameterStore.FRIEND_TOKEN;
import static com.meetme.store.ServerUrlStore.FRIEND_URL;

import java.util.Set;

import org.json.JSONObject;

import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.FriendInviteNotification;
import com.meetme.parser.FriendInviteNotificationParser;

public class FriendInviteNotificationDao extends AbstractDao<FriendInviteNotification> {

	public FriendInviteNotificationDao() {
		super(new FriendInviteNotificationParser());
	}
	
	public Set<FriendInviteNotification> findFriendInviteNotifications(String userToken) {
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_NOTIFICATION);
		parameters.put(FRIEND_TOKEN, userToken);
		
		// Send request
		responseJSON = HttpUtils.post(FRIEND_URL, parameters);
		
		// Built notification set from JSON response
		return super.findAllFromUser(responseJSON, userToken);
	}
}
