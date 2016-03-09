package com.sap.training.ems.web.controller;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sap.training.ems.persistence.model.Department;
import com.sap.training.ems.service.DepartmentService;
import com.sap.training.ems.service.EmployeeService;
import com.sap.training.ems.web.model.request.DepartmentRequest;
import com.sap.training.ems.web.model.response.DepartmentResponse;

@RestController
@RequestMapping("/department")
public class DepartmentController {
	@Autowired
	private DepartmentService departmentService;

	@Autowired
	EmployeeService employeeService;

	public Transformer<Department, DepartmentResponse> departmentTransformer = new Transformer<Department, DepartmentResponse>() {

		@Override
		public DepartmentResponse transform(Department department) {
			// TODO Auto-generated method stub
			return new DepartmentResponse(department);
		}
	};

	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody String addDepartment(
			@RequestBody DepartmentRequest departmentRequest) {
		return departmentService.addDepartment(departmentRequest);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Department getDepartment(@PathVariable("id") long id) {
		return departmentService.getDepartment(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody String updateDepartment(@PathVariable("id") long id,
			@RequestBody DepartmentRequest updatedDepartment) {
		return departmentService.updateDepartment(id, updatedDepartment);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteDepartment(@PathVariable("id") long id) {
		departmentService.deleteDepartment(id);
	}

	@RequestMapping(value = "/departments", method = RequestMethod.GET)
	public @ResponseBody Collection<DepartmentResponse> getAllDepartments() {
		return CollectionUtils.collect(departmentService.getAll(),
				departmentTransformer);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody List<Department> getAll() {
		return departmentService.getAll();
	}
}
