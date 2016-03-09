package com.sap.training.ems.service;

import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.training.ems.document.service.DocumentService;
import com.sap.training.ems.persistence.model.Department;
import com.sap.training.ems.persistence.model.Employee;
import com.sap.training.ems.persistence.repository.DepartmentRepository;
import com.sap.training.ems.persistence.repository.EmployeeRepository;
import com.sap.training.ems.web.model.request.EmployeeRequest;

@Service
@Transactional
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	public Long addEmployee(EmployeeRequest employeeRequest) {
		Employee employee = new Employee();

		employee.setName(employeeRequest.getName());
		employee.setAge(employeeRequest.getAge());
		employee.setEmail(employeeRequest.getEmail());
		employee.setPosition(employeeRequest.getPosition());
		employee.setImgId(employeeRequest.getImgId());
		if (employeeRequest.getDepartment().equals("undefined")) {
			employee.setDepartment(null);
		} else {
			Department department = departmentRepository
					.getByName(employeeRequest.getDepartment());
			employee.setDepartment(department);
			department.getMembers().add(employee);
		}
		return employeeRepository.saveAndFlush(employee).getId();
	}

	public Employee getEmployee(Long id) {
		return employeeRepository.findOne(id);
	}

	public String updateEmployee(Long id, EmployeeRequest updatedEmployee) {
		String res;
		Employee employee = employeeRepository.findOne(id);

		employee.setName(updatedEmployee.getName());
		employee.setAge(updatedEmployee.getAge());
		employee.setEmail(updatedEmployee.getEmail());
		employee.setPosition(updatedEmployee.getPosition());
		if (updatedEmployee.getDepartment().equals("undefined")) {
			employee.setDepartment(null);
		} else {
			Department newDepartment = departmentRepository
					.getByName(updatedEmployee.getDepartment());
			Department oldDepartment = employee.getDepartment();
			
			employee.setDepartment(newDepartment);
			if (oldDepartment != null && oldDepartment.getManager() != null && oldDepartment.getManager().equals(employee)) {
				oldDepartment.setManager(null);
				departmentRepository.saveAndFlush(oldDepartment);
			}
		}
		employeeRepository.saveAndFlush(employee);

		res = "Modify Employee Success!";
		return res;
	}

	public void deleteEmployee(Long id) throws NamingException {
		DocumentService documentService = new DocumentService();
		Employee employee = employeeRepository.findOne(id);
		String imgId = employee.getImgId();

		if (employee.getDepartment() != null
				&& employee.getPosition().equals("Manager")) {
			Department department = departmentRepository.findOne(employee
					.getDepartment().getId());
			department.setManager(null);
			department.getMembers().remove(employee);
		}

		employeeRepository.delete(id);
		documentService.deleteImg(imgId);
	}

	public List<Employee> getAll() {
		return employeeRepository.findAll();
	}

	public List<Department> getAllDepartments() {
		return departmentRepository.findAll();
	}

	public List<Employee> getEmployeesByDepartment(long id) {
		return employeeRepository.getEmployeesByDepartment(id);
	}
}
