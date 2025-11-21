package com.faculty.grading.repository;

import com.faculty.grading.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select c from Course c where c.faculty.id = :facultyId")
    List<Course> findByFacultyId(@Param("facultyId") Long facultyId);

    @Query("select case when count(c)>0 then true else false end from Course c where c.id = :courseId and c.faculty.id = :facultyId")
    boolean existsByIdAndFacultyId(@Param("courseId") Long courseId, @Param("facultyId") Long facultyId);
}
