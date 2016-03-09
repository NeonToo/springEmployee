package com.sap.training.ems.web.model.response;

import com.sap.training.ems.persistence.model.Department;


public class DepartmentResponse {
	private Long id;

	private String name;

	private String manager;

	public DepartmentResponse() {
	}

	public DepartmentResponse(Department department) {
		this.setId(department.getId());
		this.setName(department.getName());
		if(department.getManager() == null){
			this.setManager(null);
		} else {
			this.setManager(department.getManager().getName());
		}
	}

	public long getId() {
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

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}


}
