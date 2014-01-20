package com.meetme.parser;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.model.dao.AbstractDao;
import com.meetme.model.entity.AbstractEntity;

public abstract class AbstractParser<E extends AbstractEntity> {

	protected static final String COULD_NOT_PARSE_FIELD_FROM_JSON 
		= "Could not parse entity field from JSON  : ";
	
	// Map<"Daomethod name", "corresponding key in json">
	protected Map<String, String> JSONKeyMap = new HashMap<String, String>();
	
	/**
	 * Set the JSON key corresponding to the DAO method name
	 * so the DAO know what key to look for
	 * @param JSONKey the key in the JSON
	 */
	protected void setJSONKeyForFindAllFromUser(String JSONKey) {
		this.JSONKeyMap.put(AbstractDao.FIND_ALL_FROM_USER, JSONKey);
	}
	
	/**
	 * Fetch data from a JSON object
	 * @return the entity
	 */
	public abstract E getFromJSON(JSONObject jsonObject);
	
	/**
	 * 
	 * @param daoMethodKey the DAO method name
	 * @return the corresponding JSON key
	 */
	public String getJSONKey(String daoMethodKey) {
		String JSONKey = null;
		
		if (this.JSONKeyMap != null) {
			return this.JSONKeyMap.get(daoMethodKey);
		} 
		
		return JSONKey;
	}
	
	
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
}
