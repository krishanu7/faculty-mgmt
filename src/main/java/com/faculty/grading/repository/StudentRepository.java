package com.faculty.grading.repository;

import com.faculty.grading.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s from Student s join s.studentCourses sc where sc.course.id = :courseId")
    List<Student> findStudentsByCourseId(@Param("courseId") Long courseId);
}
