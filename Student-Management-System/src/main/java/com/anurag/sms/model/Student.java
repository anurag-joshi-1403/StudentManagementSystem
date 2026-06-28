package com.anurag.sms.model;

public class Student {
    private int id;
    private String name;
    private String email;
    private String course;
    private double  marks;

    // Default Constructor
    public Student(){
    }

    // Constructor without ID
    public Student(String name, String email, String course, double marks){
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    // Constructor with ID
    public Student(int id, String name, String email, String course, double marks){
        this.id=id;
        this.name= name;
        this.email= email;
        this.course=course;
        this.marks = marks;
    }
}
