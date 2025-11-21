package com.faculty.grading.entity;

import javax.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code")
    private String courseCode;

    private String name;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Employee faculty;

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getFaculty() {
        return faculty;
    }

    public void setFaculty(Employee faculty) {
        this.faculty = faculty;
    }
}
