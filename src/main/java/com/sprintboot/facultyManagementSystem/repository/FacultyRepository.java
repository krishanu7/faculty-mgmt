package com.sprintboot.facultyManagementSystem.repository;

import com.sprintboot.facultyManagementSystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Employee, Long> {
}
