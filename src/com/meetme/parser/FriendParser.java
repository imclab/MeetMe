package com.meetme.parser;

import java.util.Map;

import org.json.JSONObject;

import com.meetme.model.entity.Friend;

public class FriendParser extends AbstractParser<Friend> {
	private static final String JSON_KEY_FOR_FIND_ALL_FROM_USER = "friends";
	
	private static final String USER_ID = "user_id";
	private static final String FIRSTNAME = "firstname";
	private static final String LASTNAME = "lastname";
	
	private static String[] fieldNameArray = 
		{USER_ID, FIRSTNAME, LASTNAME};
	
	/*
	 * Constructor
	 */
	public FriendParser() {
		super.setJSONKeyForFindAllFromUser(JSON_KEY_FOR_FIND_ALL_FROM_USER);
	}
	
	/*
	 * Methods
	 */
	@Override
	public Friend getFromJSON(JSONObject friendJSON) {
		Friend friend = null;
		
		Map<String, String> fieldMap = 
				super.getFieldMap(
						fieldNameArray, 
						friendJSON
					);
		
		// Build friend object
		friend = new Friend(
				Integer.parseInt(fieldMap.get(USER_ID)),
				fieldMap.get(FIRSTNAME),
				fieldMap.get(LASTNAME)
			);
		
		return friend;
	}
}
