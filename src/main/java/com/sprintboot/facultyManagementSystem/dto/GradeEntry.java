package com.sprintboot.facultyManagementSystem.dto;

public class GradeEntry {
    private Long studentId;
    private Double marks;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }
}
