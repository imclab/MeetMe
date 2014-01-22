package com.meetme.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.model.entity.GoogleDirection;

public class GoogleDirectionParser {

	/*
	 * Methods
	 */
	public GoogleDirection getFromJSON(JSONObject directionJSON) {
		GoogleDirection googleDirection = null;
		
		String userEta = null;
		String userEtaSeconds = null;
		String userEda = null;
		
		try {
			JSONArray routes = directionJSON.getJSONArray("routes");
			JSONArray legs  = routes.getJSONObject(0).getJSONArray("legs");
			userEta = legs.getJSONObject(0).getJSONObject("duration").get("text").toString();
			userEtaSeconds = legs.getJSONObject(0).getJSONObject("duration").get("value").toString();
			userEda = legs.getJSONObject(0).getJSONObject("distance").get("text").toString();
		} catch (JSONException e) {
			Log.w(GoogleDirectionParser.class.getName(), AbstractParser.COULD_NOT_PARSE_FIELD_FROM_JSON + e.getMessage(), e);
		} catch (Exception e) {
			Log.e(GoogleDirectionParser.class.getName(), e.getMessage(), e);
		}
		
		// Build google direction object
		googleDirection = new GoogleDirection(
				userEta,
				Long.parseLong(userEtaSeconds),
				userEda
			);
		
		return googleDirection;
	}
}
