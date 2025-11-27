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
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private static final String CLIENT_ID = "192670555270-ta3apcff43lffk8ld2r10rvnj0htfm61.apps.googleusercontent.com";

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

    public Employee login(String email, String password) {
        Optional<Employee> opt = facultyRepository.findByEmail(email);
        if (opt.isPresent()) {
            Employee e = opt.get();
            // TODO: Use hashed password comparison in production
            if (password.equals(e.getPassword())) {
                return e;
            }
        }
        return null;
    }

    public Employee loginWithGoogle(String token) {
        try {
            com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier verifier = new com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder(
                    new com.google.api.client.http.javanet.NetHttpTransport(),
                    new com.google.api.client.json.gson.GsonFactory()).setAudience(Collections.singletonList(CLIENT_ID))
                    // Or, if multiple clients access the backend:
                    // .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                    .build();

            com.google.api.client.googleapis.auth.oauth2.GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();

                Optional<Employee> opt = facultyRepository.findByEmail(email);
                if (opt.isPresent()) {
                    return opt.get();
                } else {
                    System.out.println("User not found for email: " + email);
                    return null;
                }
            } else {
                System.out.println("Invalid ID token.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        com.sprintboot.facultyManagementSystem.model.FacultyCourse mapping = facultyCourseRepository
                .findByEmployee_IdAndCourse_Id(facultyId, courseId);
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
