package com.meetme.model.dao;

import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.meetme.model.entity.AbstractEntity;
import com.meetme.parser.AbstractParser;

public abstract class AbstractDao <E extends AbstractEntity> {
	// Method names used for the mapping with JSON key from the entity parser
	public static final String FIND_ALL_FROM_USER = "findAllFromUser";
	
	// The injected entity parser
	protected AbstractParser<E> entityParser;
	
	/*
	 * Constructor
	 */
	protected AbstractDao(AbstractParser<E> entityParser) {
		this.entityParser = entityParser;
	}
	
	/**
	 * 
	 * @param responseJSON the JSON array to parse
	 * @param userToken the user's token
	 * @return A set of the entity
	 */
	protected Set<E> findAllFromUser(
			JSONObject responseJSON,
			String userToken) {
		Set<E> entitySet = new TreeSet<E>();
		
		try {
			JSONArray entitiesJSON = (JSONArray)responseJSON.get(this.entityParser.getJSONKey(FIND_ALL_FROM_USER));
			int entitiesSize = entitiesJSON.length();
			
			for (int i = 0; i < entitiesSize; i++) {
				JSONObject entityJSON = entitiesJSON.getJSONObject(i);
				entitySet.add(this.entityParser.getFromJSON(entityJSON));
			}
			
		} catch (JSONException e) {
			Log.e(AbstractDao.class.getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(AbstractDao.class.getName(), e.getMessage(), e);
		}
		
		return entitySet;
	}
}
