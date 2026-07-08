package com.anurag.sms.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String course;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String address;

    @Column(name = "photo")
    private String photo;

    // Default Constructor
    public Student() {

    }

    // Parameterized Constructor
    public Student(Long id, String firstName, String lastName, String email, String phone, String gender, String course,
            LocalDate dateOfBirth, String address, String photo) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.course = course;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.photo = photo;
    }

    // Creating Getter and Setter................................
    public String getFirstName(String firstName) {
        return firstName;
    }

    public void setFirstName() {
        this.firstName = firstName;
    }

    public String getLastName(String lastName) {
        return firstName;
    }

    public void setLastName() {
        this.lastName = lastName;
    }

    public String getEmail(String email) {
        return email;
    }

    public void email() {
        this.email = email;
    }

    public int getPhone(int phone) {
        return phone;
    }

    public void setPhone(){
        this.phone = phone;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCourse(String course) {
        return course;
    }

    public void setCourse() {
        this.course = course;
    }

    public LocalDate getDateOfBirth(LocalDate dateOfBirth) {
        return dateOfBirth;
    }

    public void setDateOfBirth() {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress(String address) {
        return address;
    }

    public void setAddress() {
        this.address = address;
    }

    public String getPhoto(String photo) {
        return photo;
    }

    public void setPhoto() {
        this.photo = photo;
    }
}
