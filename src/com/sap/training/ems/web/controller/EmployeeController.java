package com.sap.training.ems.web.controller;

import java.util.Collection;
import javax.naming.NamingException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sap.training.ems.persistence.model.Employee;
import com.sap.training.ems.service.DepartmentService;
import com.sap.training.ems.service.EmployeeService;
import com.sap.training.ems.web.model.request.EmployeeRequest;
import com.sap.training.ems.web.model.response.EmployeeResponse;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private DepartmentService departmentService;
	
	public Transformer<Employee, EmployeeResponse> employeeTransformer =  new Transformer<Employee, EmployeeResponse>() {

		@Override
		public EmployeeResponse transform(Employee employee) {
			// TODO Auto-generated method stub
			return new EmployeeResponse(employee);
		}
	};
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody Long addEmployee(@RequestBody EmployeeRequest employeeRequest) {
		return employeeService.addEmployee(employeeRequest);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody EmployeeResponse getEmployee(@PathVariable("id") long id) {
		return new EmployeeResponse(employeeService.getEmployee(id));
	}
	
	@RequestMapping(value = "/department/{id}", method = RequestMethod.GET)
	public @ResponseBody Collection<EmployeeResponse> getEmployeesByDepartment(@PathVariable("id") long id){
		System.out.println("Department ID: " + id);
		return CollectionUtils.collect(employeeService.getEmployeesByDepartment(id), employeeTransformer);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody String updateEmployee(@PathVariable("id") long id, @RequestBody EmployeeRequest updatedEmployee) {
		return employeeService.updateEmployee(id, updatedEmployee);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteEmployee(@PathVariable("id") long id) throws NamingException {
		employeeService.deleteEmployee(id);
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody Collection<EmployeeResponse> getAll() {
		return CollectionUtils.collect(employeeService.getAll(), employeeTransformer);
	}
	
//	@RequestMapping(value = "/departments", method = RequestMethod.GET)
//	public @ResponseBody Collection<EmployeeResponse> getAllEmployees() {
//		return CollectionUtils.collect(employeeService.getAll(), employeeTransformer);
//	}
}
