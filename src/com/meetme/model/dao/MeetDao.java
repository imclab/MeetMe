package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.*;
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
import com.meetme.parser.MeetParser;

public class MeetDao {

	private MeetParser entityParser = null;
	
	public MeetDao(){
		this.entityParser = new MeetParser();
	}
	
	/**
	 * Returns all the meets associated with the meeting excepted the one about the user
	 * @param meeting 
	 * @param userToken the user token
	 * @return the set of Meet from meeting
	 */
	public Set<Meet> findOtherMeetsOfMeeting(Meeting meeting, String userToken) {
		Set<Meet> meetSet = new HashSet<Meet>();
		
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(MEET_OPERATION, MEET_OPERATION_REFRESH_OTHERS);
		parameters.put(MEET_TOKEN, userToken);
		parameters.put(MEET_REFRESH_OTHERS_MEETING_ID, Integer.toString(meeting.getId()));
		
		// Send request
		responseJSON = HttpUtils.post(MEET_URL, parameters);
		
		// Built meet set from JSON response
		try {
			JSONArray meetsJSON = (JSONArray)responseJSON.get("users");
			int meetsSize = meetsJSON.length();
			
			for (int i = 0; i < meetsSize; i++) {
				JSONObject meetJSON = meetsJSON.getJSONObject(i);
				meetSet.add(this.entityParser.getFromJSON(meetJSON, meeting.getId()));
			}
			
		} catch (JSONException e) {
			Log.e(MeetDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(MeetDao.class.getName(), e.getMessage(), e);
		}
		
		return meetSet;
	}
	
	/**
	 Returns all the meets associated with the meeting excepted the one about the user
	 * @param meeting 
	 * @param meet
	 * @param userToken
	 * @return the set of Meet from meeting
	 */
	public Set<Meet> findAllMeetsOfMeeting(Meeting meeting, Meet meet, String userToken) {
		Set<Meet> meetSet = new HashSet<Meet>();
		
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(MEET_OPERATION, MEET_OPERATION_REFRESH);
		parameters.put(MEET_TOKEN, userToken);
		parameters.put(MEET_REFRESH_MEETING_ID, Integer.toString(meeting.getId()));
		parameters.put(MEET_REFRESH_USER_EDA, meet.getUserEstimatedDistance());
		parameters.put(MEET_REFRESH_USER_ETA, meet.getUserEstimatedTime());
		parameters.put(MEET_REFRESH_USER_ETA_SECONDS, Long.toString(meet.getUserEstimatedTimeSeconds()));
		parameters.put(MEET_REFRESH_USER_LAT_LONG, meet.getUserLatitudeLongitude());
		parameters.put(MEET_REFRESH_USER_TRAVEL_MODE, Integer.toString(meet.getUserTravelMode()));
		
		// Send request
		responseJSON = HttpUtils.post(MEET_URL, parameters);
		
		// Built meet set from JSON response
		try {
			JSONArray meetsJSON = (JSONArray)responseJSON.get("users");
			int meetsSize = meetsJSON.length();
			
			for (int i = 0; i < meetsSize; i++) {
				JSONObject meetJSON = meetsJSON.getJSONObject(i);
				meetSet.add(this.entityParser.getFromJSON(meetJSON, meeting.getId()));
			}
			
		} catch (JSONException e) {
			Log.e(MeetDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(MeetDao.class.getName(), e.getMessage(), e);
		}
		
		return meetSet;
	}
}
