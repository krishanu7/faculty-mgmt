package com.sprintboot.facultyManagementSystem.repository;

import com.sprintboot.facultyManagementSystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

