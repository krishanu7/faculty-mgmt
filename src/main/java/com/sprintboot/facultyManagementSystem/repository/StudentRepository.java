package com.sprintboot.facultyManagementSystem.repository;

import com.sprintboot.facultyManagementSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
