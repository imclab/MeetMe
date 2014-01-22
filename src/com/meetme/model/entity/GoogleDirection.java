package com.meetme.model.entity;

import java.io.Serializable;


public class GoogleDirection implements Serializable {
	
	static final long serialVersionUID = 99L;
	
	protected String userEta;
	protected long userEtaSeconds;
	protected String userEda;
	
	/*
	 * Constructor
	 */
	public GoogleDirection(String userEta, long userEtaSeconds, String userEda) {
		super();
		this.userEta = userEta;
		this.userEtaSeconds = userEtaSeconds;
		this.userEda = userEda;
	}
	
	/*
	 * Methods
	 */
	@Override
	public String toString() {
		return this.userEda + ", " + this.userEta + ", " + this.userEtaSeconds;
	}
	
	/*
	 * Accessors
	 */
	public String getUserEta() {
		return userEta;
	}

	public void setUserEta(String userEta) {
		this.userEta = userEta;
	}

	public long getUserEtaSeconds() {
		return userEtaSeconds;
	}

	public void setUserEtaSeconds(long userEtaSeconds) {
		this.userEtaSeconds = userEtaSeconds;
	}

	public String getUserEda() {
		return userEda;
	}

	public void setUserEda(String userEda) {
		this.userEda = userEda;
	}
}
