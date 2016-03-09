package com.sap.training.ems.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.training.ems.persistence.model.Department;
import com.sap.training.ems.persistence.model.Employee;
import com.sap.training.ems.persistence.repository.DepartmentRepository;
import com.sap.training.ems.persistence.repository.EmployeeRepository;
import com.sap.training.ems.web.model.request.DepartmentRequest;

@Service
@Transactional
public class DepartmentService {
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public String addDepartment(DepartmentRequest departmentRequest) {
		String res;
		Department department = new Department();
		String managerName = departmentRequest.getManager();
		
		department.setName(departmentRequest.getName());
		
		if(managerName.equals("undefined")){
			department.setManager(null);
			department.setMembers(null);
			departmentRepository.saveAndFlush(department);
		} else {
			Employee manager = employeeRepository.getByName(managerName);
			if(manager != null){
				department.setManager(manager);
				
				List<Employee> members = new ArrayList<Employee>();
				members.add(manager);
				department.setMembers(members);
			} else {
				res = "Such a employee do not exist!";
			}
			departmentRepository.saveAndFlush(department);
			manager.setDepartment(department);
			manager.setPosition("Manager");
		}

		res = "Add Department Success!";
		
		return res;
	}
	
	public Department getDepartment(long id) {
		return departmentRepository.findOne(id);
	}
	
	public String updateDepartment(long id, DepartmentRequest updatedDepartment) {
		String res;
		Department department = departmentRepository.findOne(id);
		Employee oldManager = department.getManager();
		Employee newManager = employeeRepository.getByName(updatedDepartment.getManager());
		
		department.setName(updatedDepartment.getName());
		department.setManager(newManager);
		departmentRepository.saveAndFlush(department);
		newManager.setDepartment(department);
		newManager.setPosition("Manager");
		
		if(oldManager != null){
			oldManager.setPosition("Officer");
		}
		
		res = "Modify Department Success!";
		
		return res;
	}
	
	public String deleteDepartment(long id){
		String res;
		Department department = departmentRepository.findOne(id);
		List<Employee> members = department.getMembers();
		int num = members.size();
		
		for(int i = 0;i < num;i++){
			Employee employee = members.get(i);
			
			employee.setPosition("Officer");
			employee.setDepartment(null);
		}
		
		departmentRepository.delete(id);
		res = "Delete Department Success!";
		
		return res;
	}
	
	public List<Department> getAll() {
		return departmentRepository.findAll();
	}
}
