package com.meetme.protocol;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class HttpParameters extends ArrayList<NameValuePair> {
	static final long serialVersionUID = 305L; 
	
	/*
	 * Methods
	 */
	public void put(String key, String value) {
		this.add(new BasicNameValuePair(key, value));
	}
}
