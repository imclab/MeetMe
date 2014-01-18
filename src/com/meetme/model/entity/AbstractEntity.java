package com.meetme.model.entity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public abstract class AbstractEntity {
	
	protected AbstractEntity() {
	}
	
	protected static final String COULD_NOT_PARSE_FIELD_FROM_JSON 
		= "Could not parse entity field from JSON  : ";
	
	/*
	 * Get a Map<"FieldName", "FieldValue"> for className 
	 * built from JSON parsing
	 */
	protected static Map<String, String> getFieldMap(
			String[] fieldNameArray, 
			JSONObject entityJSON,
			String className) {
		
		Map<String, String> fieldMap = new HashMap<String, String>();
		
		// Init map with 0 values
		for (String fieldName : fieldNameArray) {
				fieldMap.put(fieldName, "0");
		}
		
		for (String fieldName : fieldNameArray) {
			try {
				String fieldValue = entityJSON.get(fieldName).toString();
				fieldMap.put(fieldName, fieldValue);
			} catch (JSONException e) {
				Log.w(className, COULD_NOT_PARSE_FIELD_FROM_JSON + e.getMessage(), e);
			} catch (Exception e) {
				Log.e(className, e.getMessage(), e);
			}
		}
		
		return fieldMap;
	}
}
