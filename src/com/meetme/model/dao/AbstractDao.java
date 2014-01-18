package com.meetme.model.dao;

import java.util.Set;
import java.util.TreeSet;

import org.json.JSONObject;

import com.meetme.model.entity.AbstractEntity;
import com.meetme.parser.AbstractEntityParser;

public abstract class AbstractDao <E extends AbstractEntity> {
	protected AbstractEntityParser<E> entityParser;
	
	protected AbstractDao(AbstractEntityParser<E> entityParser) {
		this.entityParser = entityParser;
	}
	
	public Set<E> findAllFromUser(String userToken) {
		Set<E> entitySet = new TreeSet<E>();
		
		E e = entityParser.getFromJSON(new JSONObject());
		entitySet.add(e);
		
		return entitySet;
	}
}
