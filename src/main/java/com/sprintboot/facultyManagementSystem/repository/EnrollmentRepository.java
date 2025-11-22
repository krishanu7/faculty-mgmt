package com.sprintboot.facultyManagementSystem.repository;

import com.sprintboot.facultyManagementSystem.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<StudentCourse, Long> {
    List<StudentCourse> findByCourse_Id(Long courseId);
}
