package com.faculty.grading.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class GradeRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    @NotNull
    @Min(0)
    private Double marks;

    private String grade;

    private String comments;

    public GradeRequest() {
    }

    public GradeRequest(Long studentId, Long courseId, Double marks, String grade, String comments) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
        this.grade = grade;
        this.comments = comments;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}