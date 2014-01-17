package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.MEET_OPERATION;
import static com.meetme.store.ServerParameterStore.MEET_OPERATION_REFRESH_OTHERS;
import static com.meetme.store.ServerParameterStore.MEET_REFRESH_OTHERS_MEETING_ID;
import static com.meetme.store.ServerParameterStore.MEET_TOKEN;
import static com.meetme.store.ServerUrlStore.MEET_URL;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.Meet;
import com.meetme.model.entity.Meeting;

public abstract class MeetDao {

	private MeetDao(){
	}
	
	public static Set<Meet> findMeetsOfMeeting(Meeting meeting, String userToken) {
		Set<Meet> meetSet = new HashSet<Meet>();
		
		JSONObject responseJSON = null;
		String url = MEET_URL;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(MEET_OPERATION, MEET_OPERATION_REFRESH_OTHERS);
		parameters.put(MEET_TOKEN, userToken);
		parameters.put(MEET_REFRESH_OTHERS_MEETING_ID, Integer.toString(meeting.getId()));
		
		// Send request
		responseJSON = HttpUtils.post(url, parameters);
		
		// Built friend list from JSON response
		try {
			JSONArray meetsJSON = (JSONArray)responseJSON.get("users");
			int meetsSize = meetsJSON.length();
			
			for (int i = 0; i < meetsSize; i++) {
				JSONObject meetJSON = meetsJSON.getJSONObject(i);
				meetSet.add(Meet.getFromJSON(meetJSON, meeting.getId()));
			}
			
		} catch (JSONException e) {
			Log.e(MeetDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(MeetDao.class.getName(), e.getMessage(), e);
		}
		
		return meetSet;
	}
}
