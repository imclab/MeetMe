package com.meetme.model.entity;

import java.io.Serializable;

public class Friend extends AbstractEntity
	implements Comparable<Friend>, Serializable {
	
	static final long serialVersionUID = 200L;
	
	protected int id;
	protected String firstname;
	protected String lastname;
	
	/*
	 * Constructors
	 */
	public Friend() {
	}
	
	public Friend(
			int id, 
			String firstname, 
			String lastname) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	/*
	 * Methods
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Friend) ) {
			return false;
		} else {
			Friend other = (Friend)o;
			return this.id == other.id;
		}
	}

	@Override
	public int hashCode() {
		return 4 * id + 3;
	}
	
	@Override
	public int compareTo(Friend another) {
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
}
