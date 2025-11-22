package com.sprintboot.facultyManagementSystem.dto;

public class StudentDTO {
    private Long id;
    private String rollNumber;
    private String firstName;
    private String lastName;

    public StudentDTO() {}

    public StudentDTO(Long id, String rollNumber, String firstName, String lastName) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.firstName = firstName;
        this.lastName = lastName;
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
}
