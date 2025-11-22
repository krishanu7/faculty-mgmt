package com.sprintboot.facultyManagementSystem.dto;

public class StudentWithGradeDTO {
    private Long id;
    private String rollNumber;
    private String firstName;
    private String lastName;
    private Double marks; // nullable if not graded

    public StudentWithGradeDTO() {}

    public StudentWithGradeDTO(Long id, String rollNumber, String firstName, String lastName, Double marks) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.marks = marks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }
}
