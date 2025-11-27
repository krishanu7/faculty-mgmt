package com.sprintboot.facultyManagementSystem.controller;

import com.sprintboot.facultyManagementSystem.dto.CourseDTO;
import com.sprintboot.facultyManagementSystem.dto.EmployeeLoginRequest;
import com.sprintboot.facultyManagementSystem.dto.GradeBatchRequest;
import com.sprintboot.facultyManagementSystem.dto.StudentWithGradeDTO;
import com.sprintboot.facultyManagementSystem.model.Employee;
import com.sprintboot.facultyManagementSystem.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping("/employee/login")
    public ResponseEntity<?> login(@RequestBody EmployeeLoginRequest request) {
        Employee f;
        if (request.getGoogleToken() != null && !request.getGoogleToken().isEmpty()) {
            f = facultyService.loginWithGoogle(request.getGoogleToken());
        } else {
            f = facultyService.login(request.getEmail(), request.getPassword());
        }

        if (f == null) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        return ResponseEntity.ok(f);
    }

    @GetMapping("/employee/{facultyId}/courses")
    public ResponseEntity<List<CourseDTO>> courses(@PathVariable Long facultyId) {
        return ResponseEntity.ok(facultyService.getCoursesByFaculty(facultyId));
    }

    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<List<StudentWithGradeDTO>> students(@PathVariable Long courseId) {
        return ResponseEntity.ok(facultyService.getStudentsByCourse(courseId));
    }

    @PostMapping("/courses/{courseId}/grades")
    public ResponseEntity<?> gradeStudents(@PathVariable Long courseId, @RequestBody GradeBatchRequest request) {
        try {
            List<StudentWithGradeDTO> updated = facultyService.gradeStudents(courseId, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        }
    }
}
