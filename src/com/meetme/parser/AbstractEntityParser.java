package com.meetme.parser;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.model.entity.AbstractEntity;

public abstract class AbstractEntityParser<E extends AbstractEntity> {

	protected static final String COULD_NOT_PARSE_FIELD_FROM_JSON 
		= "Could not parse entity field from JSON  : ";
	
	public abstract E getFromJSON(JSONObject jsonObject);
	
	/*
	 * Get a Map<"FieldName", "FieldValue"> for className 
	 * built from JSON parsing
	 */
	protected Map<String, String> getFieldMap(
			String[] fieldNameArray, 
			JSONObject entityJSON) {
		
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
				Log.w(AbstractEntityParser.class.getName(), COULD_NOT_PARSE_FIELD_FROM_JSON + e.getMessage(), e);
			} catch (Exception e) {
				Log.e(AbstractEntityParser.class.getName(), e.getMessage(), e);
			}
		}
		
		return fieldMap;
	}
}
