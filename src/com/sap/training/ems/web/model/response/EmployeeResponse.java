package com.sap.training.ems.web.model.response;

import com.sap.training.ems.persistence.model.Employee;

public class EmployeeResponse {
	private Long id;
	private String name;
	private int age;
	private String email;
	private String position;
	private String department;
	private String imgId;

	public EmployeeResponse(Employee employee) {
		this.setId(employee.getId());
		this.setName(employee.getName());
		this.setAge(employee.getAge());
		this.setEmail(employee.getEmail());
		this.setPosition(employee.getPosition());
		if (employee.getDepartment() == null) {
			this.setDepartment("undefined");
		} else {
			this.setDepartment(employee.getDepartment().getName());
		}
		this.setImgId(employee.getImgId());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

}
