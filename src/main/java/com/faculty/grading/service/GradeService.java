package com.faculty.grading.service;

import com.faculty.grading.dto.CourseDto;
import com.faculty.grading.dto.GradeBatchRequest;
import com.faculty.grading.dto.GradeRequest;
import com.faculty.grading.dto.GradeResponse;
import com.faculty.grading.dto.StudentDto;
import com.faculty.grading.entity.Course;
import com.faculty.grading.entity.Grade;
import com.faculty.grading.entity.Student;
import com.faculty.grading.repository.CourseRepository;
import com.faculty.grading.repository.GradeRepository;
import com.faculty.grading.repository.StudentCourseRepository;
import com.faculty.grading.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Autowired
    private GradeRepository gradeRepository;

    public List<CourseDto> getCoursesByFaculty(Long facultyId) {
        List<Course> courses = courseRepository.findByFacultyId(facultyId);
        List<CourseDto> dtos = new ArrayList<>();
        for (Course c : courses) {
            dtos.add(new CourseDto(c.getId(), c.getCourseCode(), c.getName()));
        }
        return dtos;
    }

    public List<StudentDto> getStudentsByCourse(Long courseId) {
        List<Student> students = studentRepository.findStudentsByCourseId(courseId);
        List<StudentDto> dtos = new ArrayList<>();
        for (Student s : students) {
            dtos.add(new StudentDto(s.getId(), s.getRollNumber(), s.getFirstName(), s.getLastName()));
        }
        return dtos;
    }

    @Transactional
    public GradeResponse gradeStudent(GradeRequest request, Long facultyId) {
        // validate that faculty teaches the course
        boolean teaches = courseRepository.existsByIdAndFacultyId(request.getCourseId(), facultyId);
        if (!teaches) {
            throw new IllegalArgumentException("Faculty not authorized for this course");
        }

        // validate student enrollment
        boolean enrolled = studentCourseRepository.existsByStudentIdAndCourseId(request.getStudentId(), request.getCourseId());
        if (!enrolled) {
            throw new IllegalArgumentException("Student not enrolled in the course");
        }

        Grade grade = new Grade();
        grade.setStudentId(request.getStudentId());
        grade.setCourseId(request.getCourseId());
        grade.setMarks(request.getMarks());
        grade.setGrade(request.getGrade());
        grade.setComments(request.getComments());

        Grade saved = gradeRepository.save(grade);

        return new GradeResponse(saved.getId(), saved.getStudentId(), saved.getCourseId(), saved.getMarks(), saved.getGrade(), saved.getComments());
    }

    @Transactional
    public List<GradeResponse> gradeBatch(GradeBatchRequest request, Long facultyId) {
        // validate faculty
        boolean teaches = courseRepository.existsByIdAndFacultyId(request.getCourseId(), facultyId);
        if (!teaches) {
            throw new IllegalArgumentException("Faculty not authorized for this course");
        }

        List<GradeResponse> result = new ArrayList<>();
        for (GradeBatchRequest.GradeEntry e : request.getEntries()) {
            if (!studentCourseRepository.existsByStudentIdAndCourseId(e.getStudentId(), request.getCourseId())) {
                // skip or raise? we'll skip and continue
                continue;
            }
            Grade g = new Grade();
            g.setStudentId(e.getStudentId());
            g.setCourseId(request.getCourseId());
            g.setMarks(e.getMarks());
            g.setGrade(e.getGrade());
            g.setComments(e.getComments());
            Grade saved = gradeRepository.save(g);
            result.add(new GradeResponse(saved.getId(), saved.getStudentId(), saved.getCourseId(), saved.getMarks(), saved.getGrade(), saved.getComments()));
        }
        return result;
    }
}