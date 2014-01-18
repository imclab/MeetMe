package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION;
import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION_LIST;
import static com.meetme.store.ServerParameterStore.FRIEND_TOKEN;
import static com.meetme.store.ServerUrlStore.FRIEND_URL;

import java.util.Set;

import org.json.JSONObject;

import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.Friend;
import com.meetme.parser.FriendParser;

public class FriendDao extends AbstractDao<Friend> {
	
	public FriendDao() {
		super(new FriendParser());
	}
	
	public Set<Friend> findFriendsOfUser(String userToken) {
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_LIST);
		parameters.put(FRIEND_TOKEN, userToken);
		
		// Send request
		responseJSON = HttpUtils.post(FRIEND_URL, parameters);
		
		// Built friend set from JSON response
		return super.findAllFromUser(responseJSON, userToken);
	}
}
