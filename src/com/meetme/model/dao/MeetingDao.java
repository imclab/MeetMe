package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.MEETING_OPERATION;
import static com.meetme.store.ServerParameterStore.MEETING_OPERATION_LIST;
import static com.meetme.store.ServerParameterStore.MEETING_OPERATION_VIEW;
import static com.meetme.store.ServerParameterStore.MEETING_TOKEN;
import static com.meetme.store.ServerParameterStore.MEETING_VIEW_MEETING_ID;
import static com.meetme.store.ServerUrlStore.MEETING_URL;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.Meeting;
import com.meetme.parser.MeetingParser;

public class MeetingDao extends AbstractDao<Meeting> {

	public MeetingDao() {
		super(new MeetingParser());
	}
	
	public Set<Meeting> findMeetingsOfUser(String userToken) {
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(MEETING_OPERATION, MEETING_OPERATION_LIST);
		parameters.put(MEETING_TOKEN, userToken);
		
		// Send request
		responseJSON = HttpUtils.post(MEETING_URL, parameters);
		
		// Built meeting set from JSON response
		return super.findAllFromUser(responseJSON, userToken);
	}
	
	public Meeting findMeetingById(int meetingId, String userToken) {
		Meeting meeting = new Meeting();
		meeting.setId(meetingId);
		
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(MEETING_OPERATION, MEETING_OPERATION_VIEW);
		parameters.put(MEETING_TOKEN, userToken);
		parameters.put(MEETING_VIEW_MEETING_ID, Integer.toString(meetingId));
		
		// Send request
		responseJSON = HttpUtils.post(MEETING_URL, parameters);
		
		// Built meeting from JSON response
		try {
			meeting = this.entityParser.getFromJSON(responseJSON.getJSONObject("meeting"));
		} catch (JSONException e) {
			Log.e(MeetingDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(MeetingDao.class.getName(), e.getMessage(), e);
		}
		
		return meeting;
	}
}
