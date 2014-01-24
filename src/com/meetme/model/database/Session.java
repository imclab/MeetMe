package com.meetme.model.database;

/**
 * Handling the persistent session
 */
public class Session {
	
	private String email;
	private String password;
	
	/*
	 * Constructor
	 */
	public Session(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	
	/*
	 * Accessors
	 */
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
