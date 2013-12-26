package com.meetme.model.dao;

import static com.meetme.protocol.store.ServerParameterStore.FRIEND_OPERATION;
import static com.meetme.protocol.store.ServerParameterStore.FRIEND_OPERATION_LIST;
import static com.meetme.protocol.store.ServerParameterStore.FRIEND_TOKEN;
import static com.meetme.protocol.store.ServerUrlStore.FRIEND_URL;

import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.core.HttpUtils;
import com.meetme.model.entity.Friend;
import com.meetme.protocol.HttpParameters;

public abstract class FriendDao {

	private FriendDao(){
	}
	
	public static Set<Friend> findFriendsOfUser(String userToken) {
		Set<Friend> friendSet = new TreeSet<Friend>();
		
		JSONObject responseJSON = null;
		String url = FRIEND_URL;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_LIST);
		parameters.put(FRIEND_TOKEN, userToken);
		
		// Send request
		responseJSON = HttpUtils.post(url, parameters);
		
		// Built friend list from JSON response
		try {
			JSONArray meetingsJSON = (JSONArray)responseJSON.get("friends");
			int meetingsSize = meetingsJSON.length();
			
			for (int i = 0; i < meetingsSize; i++) {
				JSONObject meetingJSON = meetingsJSON.getJSONObject(i);
				friendSet.add(Friend.getFromJSON(meetingJSON));
			}
			
		} catch (JSONException e) {
			Log.e(FriendDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(FriendDao.class.getName(), e.getMessage(), e);
		}
		
		return friendSet;
	}
}
