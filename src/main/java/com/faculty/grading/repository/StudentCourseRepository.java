package com.faculty.grading.repository;

import com.faculty.grading.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    @Query("select case when count(sc)>0 then true else false end from StudentCourse sc where sc.student.id = :studentId and sc.course.id = :courseId")
    boolean existsByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
