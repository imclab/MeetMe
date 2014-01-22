package com.meetme.model.dao;

import static com.meetme.store.ServerParameterStore.GOOGLE_DIRECTIONS_DESTINATION;
import static com.meetme.store.ServerParameterStore.GOOGLE_DIRECTIONS_ORIGIN;
import static com.meetme.store.ServerParameterStore.GOOGLE_DIRECTIONS_SENSOR;
import static com.meetme.store.ServerParameterStore.GOOGLE_DIRECTIONS_TRAVEL_MODE;
import static com.meetme.store.ServerParameterStore.GOOGLE_DIRECTIONS_TRAVEL_MODE_BICYCLING;
import static com.meetme.store.ServerParameterStore.GOOGLE_DIRECTIONS_TRAVEL_MODE_DRIVING;
import static com.meetme.store.ServerParameterStore.GOOGLE_DIRECTIONS_TRAVEL_MODE_WALKING;
import static com.meetme.store.ServerUrlStore.GOOGLE_DIRECTIONS_URL;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_BICYCLING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_DRIVING;
import static com.meetme.store.UserTravelModeStore.TRAVEL_MODE_WALKING;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import com.meetme.core.HttpParameters;
import com.meetme.core.HttpUtils;
import com.meetme.model.entity.GoogleDirection;
import com.meetme.parser.GoogleDirectionParser;

public class GoogleDirectionDao {
	
	private GoogleDirectionParser googleDirectionParser;
	private Map<Integer, String> travelModeMap;
	
	@SuppressLint("UseSparseArrays")
	public GoogleDirectionDao() {
		this.googleDirectionParser = new GoogleDirectionParser();
		this.travelModeMap = new HashMap<Integer, String>();
		
		// init map
		this.travelModeMap.put(TRAVEL_MODE_BICYCLING, GOOGLE_DIRECTIONS_TRAVEL_MODE_BICYCLING);
		this.travelModeMap.put(TRAVEL_MODE_WALKING, GOOGLE_DIRECTIONS_TRAVEL_MODE_WALKING);
		this.travelModeMap.put(TRAVEL_MODE_DRIVING, GOOGLE_DIRECTIONS_TRAVEL_MODE_DRIVING);
	}
	
	private String getTravelModeString(int travelModeCode) {
		return this.travelModeMap.get(travelModeCode);
	}
	
	public GoogleDirection findBetweenUserLocationAndMeeting(
			String latLong,
			int userTravelMode,
			String meetingLatLong) {
		GoogleDirection googleDirection = null;
		
		JSONObject responseJSON = null;
		HttpParameters parameters = new HttpParameters();
		
		// Add parameters
		parameters.put(GOOGLE_DIRECTIONS_ORIGIN, latLong);
		parameters.put(GOOGLE_DIRECTIONS_DESTINATION, meetingLatLong );
		parameters.put(GOOGLE_DIRECTIONS_TRAVEL_MODE, getTravelModeString(userTravelMode));
		parameters.put(GOOGLE_DIRECTIONS_SENSOR, "true");
		
		// Send request
		responseJSON = HttpUtils.get(GOOGLE_DIRECTIONS_URL, parameters);
		
		// Built Google Direction from JSON response
		googleDirection = this.googleDirectionParser.getFromJSON(responseJSON);
		
		return googleDirection;
	}
}
