package com.sap.training.ems.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sap.training.ems.persistence.model.Employee;

@Repository
@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	@Query("SELECT e FROM Employee e WHERE e.name = ?1")
	Employee getByName(String name);
	
	@Query("SELECT e FROM Employee e WHERE e.department.id = ?1")
	List<Employee> getEmployeesByDepartment(long id);
}
