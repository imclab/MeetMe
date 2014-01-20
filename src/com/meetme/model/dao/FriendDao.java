package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION;
import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION_LIST;
import static com.meetme.store.ServerParameterStore.FRIEND_TOKEN;
import static com.meetme.store.ServerUrlStore.FRIEND_URL;

import java.util.Set;

import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.Friend;
import com.meetme.parser.FriendParser;

public class FriendDao extends AbstractDao<Friend> {
	
	private static final String JSON_KEY_FOR_FIND_FRENDS_OF_USER = "friends";
	
	public FriendDao() {
		super(new FriendParser());
	}
	
	public Set<Friend> findFriendsOfUser(String userToken) {
		// Add parameters
		HttpParameters parameters = new HttpParameters();
		parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_LIST);
		parameters.put(FRIEND_TOKEN, userToken);
		
		// Built friend set from JSON response
		return super.findAllFromUser(
				HttpUtils.post(FRIEND_URL, parameters), 
				JSON_KEY_FOR_FIND_FRENDS_OF_USER
			);
	}
}
