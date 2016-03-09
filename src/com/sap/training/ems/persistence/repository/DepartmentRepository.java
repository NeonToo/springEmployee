package com.sap.training.ems.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sap.training.ems.persistence.model.Department;

@Repository
@Transactional
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	@Query("SELECT d FROM Department d WHERE d.name = ?1")
	Department getByName(String name);
}
