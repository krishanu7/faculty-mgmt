package com.sprintboot.facultyManagementSystem.dto;

import java.util.List;

public class GradeBatchRequest {
    private Long employeeId;
    private List<GradeEntry> grades;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<GradeEntry> getGrades() {
        return grades;
    }

    public void setGrades(List<GradeEntry> grades) {
        this.grades = grades;
    }
}
