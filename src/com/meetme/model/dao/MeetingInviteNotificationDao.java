package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.MEETING_OPERATION;
import static com.meetme.store.ServerParameterStore.MEETING_OPERATION_NOTIFICATION;
import static com.meetme.store.ServerParameterStore.MEETING_TOKEN;
import static com.meetme.store.ServerUrlStore.MEETING_URL;

import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.MeetingInviteNotification;
import com.meetme.parser.MeetingInviteNotificationEntityParser;

public class MeetingInviteNotificationDao extends AbstractDao<MeetingInviteNotification> {
	
	public MeetingInviteNotificationDao() {
		super(new MeetingInviteNotificationEntityParser());
	}
	
	public Set<MeetingInviteNotification> findMeetingInviteNotifications(String userToken) {
		Set<MeetingInviteNotification> notificationSet = new TreeSet<MeetingInviteNotification>();
		
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(MEETING_OPERATION, MEETING_OPERATION_NOTIFICATION);
		parameters.put(MEETING_TOKEN, userToken);
		
		// Send request
		responseJSON = HttpUtils.post(MEETING_URL, parameters);
		
		// Built notification set from JSON response
		try {
			JSONArray notificationsJSON = (JSONArray)responseJSON.get("meetings");
			int notificationsSize = notificationsJSON.length();
			
			for (int i = 0; i < notificationsSize; i++) {
				JSONObject notificationJSON = notificationsJSON.getJSONObject(i);
				notificationSet.add(this.entityParser.getFromJSON(notificationJSON));
			}
			
		} catch (JSONException e) {
			Log.e(MeetingInviteNotificationDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(MeetingInviteNotificationDao.class.getName(), e.getMessage(), e);
		}
		
		return notificationSet;
	}
}
