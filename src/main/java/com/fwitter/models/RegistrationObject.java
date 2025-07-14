package com.fwitter.models;

import java.sql.Date;

public class RegistrationObject {
	

	private String fullName;
	private String email;
	private Date dob;
	public RegistrationObject() {
		super();
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "RegistrationObject{" +
				"fullName='" + fullName + '\'' +
				", email='" + email + '\'' +
				", dob=" + dob +
				'}';
	}
}
