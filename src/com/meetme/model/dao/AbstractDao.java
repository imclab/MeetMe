package com.meetme.model.dao;

import java.util.Set;

import org.json.JSONObject;

import com.meetme.model.entity.AbstractEntity;
import com.meetme.parser.AbstractParser;

public abstract class AbstractDao <E extends AbstractEntity> {
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
	 * @param JSONKey the JSON key of the entity to find
	 * @param userToken the user's token
	 * @return A set of the entity
	 */
	protected Set<E> findAllFromUser(
			JSONObject responseJSON,
			String JSONKey) {
		return this.entityParser.getSetFromJSON(responseJSON, JSONKey);
	}
}
