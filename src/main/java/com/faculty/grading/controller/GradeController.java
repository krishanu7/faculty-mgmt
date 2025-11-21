package com.faculty.grading.controller;

import com.faculty.grading.dto.CourseDto;
import com.faculty.grading.dto.GradeBatchRequest;
import com.faculty.grading.dto.GradeRequest;
import com.faculty.grading.dto.GradeResponse;
import com.faculty.grading.dto.StudentDto;
import com.faculty.grading.repository.EmployeeRepository;
import com.faculty.grading.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    // List courses taught by a faculty. If authenticated, faculty id is resolved from Principal; alternatively pass header X-Faculty-Id
    @GetMapping("/faculty/courses")
    public ResponseEntity<List<CourseDto>> getCoursesForFaculty(
            @RequestHeader(value = "X-Faculty-Id", required = false) Long facultyId,
            Principal principal) {
        if (facultyId == null && principal != null) {
            facultyId = employeeRepository.findIdByEmail(principal.getName());
        }
        if (facultyId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gradeService.getCoursesByFaculty(facultyId));
    }

    // List students enrolled in a course
    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<List<StudentDto>> getStudentsForCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(gradeService.getStudentsByCourse(courseId));
    }

    // Grade a single student
    @PostMapping("/grades")
    public ResponseEntity<GradeResponse> gradeStudent(
            @RequestBody @Valid GradeRequest request,
            @RequestHeader(value = "X-Faculty-Id", required = false) Long facultyId,
            Principal principal) {
        if (facultyId == null && principal != null) {
            facultyId = employeeRepository.findIdByEmail(principal.getName());
        }
        if (facultyId == null) {
            return ResponseEntity.badRequest().build();
        }
        GradeResponse resp = gradeService.gradeStudent(request, facultyId);
        return ResponseEntity.ok(resp);
    }

    // Grade multiple students in batch
    @PostMapping("/grades/batch")
    public ResponseEntity<List<GradeResponse>> gradeBatch(
            @RequestBody @Valid GradeBatchRequest request,
            @RequestHeader(value = "X-Faculty-Id", required = false) Long facultyId,
            Principal principal) {
        if (facultyId == null && principal != null) {
            facultyId = employeeRepository.findIdByEmail(principal.getName());
        }
        if (facultyId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<GradeResponse> responses = gradeService.gradeBatch(request, facultyId);
        return ResponseEntity.ok(responses);
    }
}