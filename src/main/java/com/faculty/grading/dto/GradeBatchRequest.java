package com.faculty.grading.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class GradeBatchRequest {

    @NotNull
    private Long courseId;

    @NotNull
    private List<GradeEntry> entries;

    public GradeBatchRequest() {
    }

    public GradeBatchRequest(Long courseId, List<GradeEntry> entries) {
        this.courseId = courseId;
        this.entries = entries;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<GradeEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<GradeEntry> entries) {
        this.entries = entries;
    }

    public static class GradeEntry {
        @NotNull
        private Long studentId;

        @NotNull
        private Double marks;

        private String grade;

        private String comments;

        public GradeEntry() {
        }

        public GradeEntry(Long studentId, Double marks, String grade, String comments) {
            this.studentId = studentId;
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
}