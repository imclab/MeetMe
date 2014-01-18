package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION;
import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION_LIST;
import static com.meetme.store.ServerParameterStore.FRIEND_TOKEN;
import static com.meetme.store.ServerUrlStore.FRIEND_URL;

import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.Friend;
import com.meetme.parser.FriendEntityParser;

public class FriendDao extends AbstractDao<Friend> {
	
	public FriendDao() {
		super(new FriendEntityParser());
	}
	
	public Set<Friend> findFriendsOfUser(String userToken) {
		Set<Friend> friendSet = new TreeSet<Friend>();
		
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_LIST);
		parameters.put(FRIEND_TOKEN, userToken);
		
		// Send request
		responseJSON = HttpUtils.post(FRIEND_URL, parameters);
		
		// Built friend set from JSON response
		try {
			JSONArray meetingsJSON = (JSONArray)responseJSON.get("friends");
			int meetingsSize = meetingsJSON.length();
			
			for (int i = 0; i < meetingsSize; i++) {
				JSONObject meetingJSON = meetingsJSON.getJSONObject(i);
				friendSet.add(this.entityParser.getFromJSON(meetingJSON));
			}
			
		} catch (JSONException e) {
			Log.e(FriendDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(FriendDao.class.getName(), e.getMessage(), e);
		}
		
		return friendSet;
	}
}
