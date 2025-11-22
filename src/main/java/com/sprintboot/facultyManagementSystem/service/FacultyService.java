package com.sprintboot.facultyManagementSystem.service;

import com.sprintboot.facultyManagementSystem.dto.CourseDTO;
import com.sprintboot.facultyManagementSystem.dto.GradeBatchRequest;
import com.sprintboot.facultyManagementSystem.dto.GradeEntry;
import com.sprintboot.facultyManagementSystem.dto.StudentWithGradeDTO;
import com.sprintboot.facultyManagementSystem.model.Course;
import com.sprintboot.facultyManagementSystem.model.Employee;
import com.sprintboot.facultyManagementSystem.model.FacultyCourse;
import com.sprintboot.facultyManagementSystem.model.Grade;
import com.sprintboot.facultyManagementSystem.model.Student;
import com.sprintboot.facultyManagementSystem.model.StudentCourse;
import com.sprintboot.facultyManagementSystem.repository.CourseRepository;
import com.sprintboot.facultyManagementSystem.repository.FacultyCourseRepository;
import com.sprintboot.facultyManagementSystem.repository.GradeRepository;
import com.sprintboot.facultyManagementSystem.repository.StudentCourseRepository;
import com.sprintboot.facultyManagementSystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final com.sprintboot.facultyManagementSystem.repository.EmployeeRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final GradeRepository gradeRepository;
    private final FacultyCourseRepository facultyCourseRepository;

    public FacultyService(com.sprintboot.facultyManagementSystem.repository.EmployeeRepository facultyRepository,
                          CourseRepository courseRepository,
                          StudentRepository studentRepository,
                          StudentCourseRepository studentCourseRepository,
                          GradeRepository gradeRepository,
                          FacultyCourseRepository facultyCourseRepository) {
        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.studentCourseRepository = studentCourseRepository;
        this.gradeRepository = gradeRepository;
        this.facultyCourseRepository = facultyCourseRepository;
    }

    public Employee getFacultyById(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public List<CourseDTO> getCoursesByFaculty(Long facultyId) {
        List<FacultyCourse> mappings = facultyCourseRepository.findByEmployee_Id(facultyId);
        return mappings.stream()
                .map(m -> m.getCourse())
                .map(c -> new CourseDTO(c.getId(), c.getCode(), c.getName()))
                .collect(Collectors.toList());
    }

    public List<StudentWithGradeDTO> getStudentsByCourse(Long courseId) {
        List<StudentCourse> enrollments = studentCourseRepository.findByCourse_Id(courseId);
        List<StudentWithGradeDTO> result = new ArrayList<>();
        for (StudentCourse e : enrollments) {
            Student s = e.getStudent();
            Grade g = gradeRepository.findByCourse_IdAndStudent_Id(courseId, s.getId());
            Double marks = g != null ? g.getMarks() : null;
            result.add(new StudentWithGradeDTO(s.getId(), s.getRollNumber(), s.getFirstName(), s.getLastName(), marks));
        }
        return result;
    }

    public List<StudentWithGradeDTO> gradeStudents(Long courseId, GradeBatchRequest request) {
        Long facultyId = request.getEmployeeId();
        Optional<Course> optCourse = courseRepository.findById(courseId);
        if (optCourse.isEmpty()) {
            throw new IllegalArgumentException("Course not found");
        }
        Course course = optCourse.get();
        // verify faculty (employee) teaches the course via faculty_courses mapping
        com.sprintboot.facultyManagementSystem.model.FacultyCourse mapping = facultyCourseRepository.findByEmployee_IdAndCourse_Id(facultyId, courseId);
        if (mapping == null) {
            throw new IllegalArgumentException("Faculty does not teach this course");
        }

        for (GradeEntry entry : request.getGrades()) {
            Long studentId = entry.getStudentId();
            Optional<Student> optStudent = studentRepository.findById(studentId);
            if (optStudent.isEmpty()) {
                continue; // skip unknown student
            }
            Student student = optStudent.get();
            Grade g = gradeRepository.findByCourse_IdAndStudent_Id(courseId, studentId);
            if (g == null) {
                Grade ng = new Grade();
                ng.setCourse(course);
                ng.setStudent(student);
                ng.setMarks(entry.getMarks());
                gradeRepository.save(ng);
            } else {
                g.setMarks(entry.getMarks());
                gradeRepository.save(g);
            }
        }

        return getStudentsByCourse(courseId);
    }
}
