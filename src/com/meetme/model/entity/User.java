package com.meetme.model.entity;

import java.io.Serializable;

public class User extends AbstractEntity
	implements Comparable<User>, Serializable {
	
	static final long serialVersionUID = 200L;
	
	private String token;
	private int id;
	private String firstname;
	private String lastname;
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
		this.token = token;
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
	}
	
	/*
	 * Methods
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof User) ) {
			return false;
		} else {
			User other = (User)o;
			return this.id == other.id;
		}
	}

	@Override
	public int hashCode() {
		return 4 * id + 3;
	}
	
	@Override
	public int compareTo(User another) {
		return this.lastname.compareTo(another.lastname);
	}

	@Override
	public String toString() {
		return this.firstname + " " + this.lastname;
	}

	/*
	 * Accessors 
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

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
