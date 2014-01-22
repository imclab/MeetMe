package com.meetme.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.model.entity.AbstractEntity;

public abstract class AbstractParser<E extends AbstractEntity> {

	protected static final String COULD_NOT_PARSE_FIELD_FROM_JSON 
		= "Could not parse entity field from JSON  : ";
	
	/**
	 * Get a Map<"FieldName", "FieldValue">
	 * built from JSON parsing
	 * @param fieldNameArray The list of the class fields names
	 * @param entityJSON the JSON corresponding to the entity
	 * @return Mapping of class fields and values extracted from entityJSON
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
				Log.w(AbstractParser.class.getName(), COULD_NOT_PARSE_FIELD_FROM_JSON + e.getMessage(), e);
			} catch (Exception e) {
				Log.e(AbstractParser.class.getName(), e.getMessage(), e);
			}
		}
		
		return fieldMap;
	}
	
	/**
	 * Fetch data from a JSON object
	 * @return the entity
	 */
	public abstract E getFromJSON(JSONObject jsonObject);
	
	public Set<E> getSetFromJSON(JSONObject responseJSON, String JSONKey) {
		Set<E> entitySet = new TreeSet<E>();
		
		try {
			JSONArray entitiesJSON = responseJSON.getJSONArray(JSONKey);
			int entitiesSize = entitiesJSON.length();
			
			for (int i = 0; i < entitiesSize; i++) {
				JSONObject entityJSON = entitiesJSON.getJSONObject(i);
				entitySet.add(getFromJSON(entityJSON));
			}
			
		} catch (JSONException e) {
			Log.e(AbstractParser.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(AbstractParser.class.getName(), e.getMessage(), e);
		}
		
		return entitySet;
	}
}
