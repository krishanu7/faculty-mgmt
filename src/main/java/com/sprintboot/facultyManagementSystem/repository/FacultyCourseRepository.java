package com.sprintboot.facultyManagementSystem.repository;

import com.sprintboot.facultyManagementSystem.model.FacultyCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyCourseRepository extends JpaRepository<FacultyCourse, Long> {
    List<FacultyCourse> findByEmployee_Id(Long employeeId);
    FacultyCourse findByEmployee_IdAndCourse_Id(Long employeeId, Long courseId);
}
