package com.meetme.core;

import static com.meetme.protocol.store.ServerParameterStore.MEETING_OPERATION;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_OPERATION_LIST;
import static com.meetme.protocol.store.ServerParameterStore.MEETING_TOKEN;
import static com.meetme.protocol.store.ServerUrlStore.MEETING_URL;

import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.activity.MainActivity;
import com.meetme.model.dao.FriendDao;
import com.meetme.model.entity.Friend;
import com.meetme.model.entity.Meeting;
import com.meetme.protocol.HttpParameters;

public class SessionManager {
	
	private static SessionManager instance = null;
	private String userToken = null;
	private Set<Friend> friendSet = null;
	private Set<Meeting> meetingSet = null;
	
	private SessionManager() {
		this.friendSet = new TreeSet<Friend>();
		this.meetingSet = new TreeSet<Meeting>();
	}
	
	/*
	 * Accessors
	 */
	public static SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
		}
		
		return instance;
	}
	
	public String getUserToken() {
		return this.userToken;
	}
	
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
	public Set<Friend> getFriendSet() {
		return this.friendSet;
	}
	
	public Set<Meeting> getMeetingSet() {
		return this.meetingSet;
	}
	
	/*
	 * Update methods
	 */
	public void updateFriendSet() {
		if (this.userToken != null && !this.userToken.isEmpty()) {
			this.friendSet = FriendDao.findFriendsOfUser(this.userToken);
		}
	}
	
	public void updateMeetingSet() {
		if (this.userToken != null && !this.userToken.isEmpty()) {
			JSONObject responseJSON = null;
			String url = MEETING_URL;
			HttpParameters parameters = new HttpParameters();
			
			// Add parameters
			parameters.put(MEETING_OPERATION, MEETING_OPERATION_LIST);
			parameters.put(MEETING_TOKEN, this.userToken);
			
			// Send request
			responseJSON = HttpUtils.post(url, parameters);
			
			// Built meeting list from JSON response
			try {
				JSONArray meetingsJSON = (JSONArray)responseJSON.get("meetings");
				int meetingsSize = meetingsJSON.length();
				
				for (int i = 0; i < meetingsSize; i++) {
					JSONObject meetingJSON = meetingsJSON.getJSONObject(i);
					this.meetingSet.add(Meeting.getFromJSON(meetingJSON));
				}
				
			} catch (JSONException e) {
				Log.e(MainActivity.class.getName(), e.getMessage());
			} catch (Exception e) {
				Log.e(MainActivity.class.getName(), e.getMessage());
			}
		}
	}
}
