package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION;
import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION_NOTIFICATION;
import static com.meetme.store.ServerParameterStore.FRIEND_TOKEN;
import static com.meetme.store.ServerUrlStore.FRIEND_URL;

import java.util.Set;

import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.FriendInviteNotification;
import com.meetme.parser.FriendInviteNotificationParser;

public class FriendInviteNotificationDao extends AbstractDao<FriendInviteNotification> {
	private static final String JSON_KEY_FOR_FIND_FRIEND_INVITE_NOTIFICATIONS = "friends";
	
	public FriendInviteNotificationDao() {
		super(new FriendInviteNotificationParser());
	}
	
	public Set<FriendInviteNotification> findFriendInviteNotifications(String userToken) {
		// Add parameters
		HttpParameters parameters = new HttpParameters();
		parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_NOTIFICATION);
		parameters.put(FRIEND_TOKEN, userToken);
		
		// Built notification set from JSON response
		return super.findAllFromUser(
				HttpUtils.post(FRIEND_URL, parameters), 
				JSON_KEY_FOR_FIND_FRIEND_INVITE_NOTIFICATIONS
			);
	}
}
