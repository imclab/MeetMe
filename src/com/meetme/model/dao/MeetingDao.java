package com.meetme.model.dao;

import static com.meetme.protocol.store.ServerParameterStore.*;
import static com.meetme.protocol.store.ServerUrlStore.MEETING_URL;

import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.core.HttpUtils;
import com.meetme.model.entity.Meeting;
import com.meetme.protocol.HttpParameters;

public abstract class MeetingDao {

	private MeetingDao() {
	}
	
	public static Set<Meeting> findMeetingsOfUser(String userToken) {
		Set<Meeting> meetingSet = new TreeSet<Meeting>();
		
		JSONObject responseJSON = null;
		String url = MEETING_URL;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(MEETING_OPERATION, MEETING_OPERATION_LIST);
		parameters.put(MEETING_TOKEN, userToken);
		
		// Send request
		responseJSON = HttpUtils.post(url, parameters);
		
		// Built meeting list from JSON response
		try {
			JSONArray meetingsJSON = (JSONArray)responseJSON.get("meetings");
			int meetingsSize = meetingsJSON.length();
			
			for (int i = 0; i < meetingsSize; i++) {
				JSONObject meetingJSON = meetingsJSON.getJSONObject(i);
				meetingSet.add(Meeting.getFromJSON(meetingJSON));
			}
			
		} catch (JSONException e) {
			Log.e(MeetingDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(MeetingDao.class.getName(), e.getMessage(), e);
		}
		
		return meetingSet;
	}
	
	public static Meeting findMeetingById(int meetingId, String userToken) {
		Meeting meeting = new Meeting();
		meeting.setId(meetingId);
		
		JSONObject responseJSON = null;
		String url = MEETING_URL;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(MEETING_OPERATION, MEETING_OPERATION_VIEW);
		parameters.put(MEETING_TOKEN, userToken);
		parameters.put(MEETING_VIEW_MEETING_ID, Integer.toString(meetingId));
		
		// Send request
		responseJSON = HttpUtils.post(url, parameters);
		
		// Built meeting from JSON response
		try {
			meeting = Meeting.getFromJSON(responseJSON.getJSONObject("meeting"));
		} catch (JSONException e) {
			Log.e(MeetingDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(MeetingDao.class.getName(), e.getMessage(), e);
		}
		
		return meeting;
	}
}
