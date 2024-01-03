package com.devashish.beans;

public class Employee {
	private Person person;
	private String employeeId;
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}	
}
