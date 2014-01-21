package com.meetme.parser;

import java.util.Map;

import org.json.JSONObject;

import com.meetme.model.entity.User;

public class UserParser extends AbstractParser<User> {

	private static final String TOKEN = "token";
	private static final String USER_ID = "user_id";
	private static final String FIRSTNAME = "firstname";
	private static final String LASTNAME = "lastname";
	private static final String EMAIL = "email";
	
	private static String[] fieldNameArray = 
		{TOKEN, USER_ID, FIRSTNAME, LASTNAME, EMAIL};
	
	/*
	 * Methods
	 */
	@Override
	public User getFromJSON(JSONObject userJSON) {
		User user = null;
		
		Map<String, String> fieldMap = 
				super.getFieldMap(
						fieldNameArray, 
						userJSON
					);
		
		// Build user object
		user = new User(
				fieldMap.get(TOKEN),
				Integer.parseInt(fieldMap.get(USER_ID)),
				fieldMap.get(FIRSTNAME),
				fieldMap.get(LASTNAME),
				fieldMap.get(EMAIL)
			);
		
		return user;
	}
}
