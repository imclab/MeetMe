package com.meetme.model.entity;

public class User extends Friend {
	
	static final long serialVersionUID = 286L;
	
	private String token;
	private String email;
	
	/*
	 * Constructors
	 */
	public User() {
	}
	
	public User(
			String token, 
			int id, 
			String firstname, 
			String lastname,
			String email) {
		super(id, firstname, lastname);
		this.token = token;
		this.email = email;
	}

	/*
	 * Accessors 
	 */
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
