package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION;
import static com.meetme.store.ServerParameterStore.FRIEND_OPERATION_NOTIFICATION;
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
import com.meetme.model.entity.FriendInviteNotification;
import com.meetme.parser.FriendInviteNotificationEntityParser;

public class FriendInviteNotificationDao extends AbstractDao<FriendInviteNotification> {

	public FriendInviteNotificationDao() {
		super(new FriendInviteNotificationEntityParser());
	}
	
	public Set<FriendInviteNotification> findFriendInviteNotifications(String userToken) {
		Set<FriendInviteNotification> notificationSet = new TreeSet<FriendInviteNotification>();
		
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(FRIEND_OPERATION, FRIEND_OPERATION_NOTIFICATION);
		parameters.put(FRIEND_TOKEN, userToken);
		
		// Send request
		responseJSON = HttpUtils.post(FRIEND_URL, parameters);
		
		// Built notification set from JSON response
		try {
			JSONArray notificationsJSON = (JSONArray)responseJSON.get("friends");
			int notificationsSize = notificationsJSON.length();
			
			for (int i = 0; i < notificationsSize; i++) {
				JSONObject notificationJSON = notificationsJSON.getJSONObject(i);
				notificationSet.add(this.entityParser.getFromJSON(notificationJSON));
			}
			
		} catch (JSONException e) {
			Log.e(FriendInviteNotificationDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(FriendInviteNotificationDao.class.getName(), e.getMessage(), e);
		}
		
		return notificationSet;
	}
}
