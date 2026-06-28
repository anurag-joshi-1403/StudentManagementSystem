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

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getCourse(){
        return course;
    }
    public void setCourse(String course){
        this.course=course;
    }
    public double getMarks(){
        return marks;
    }
    public void setMarks(double marks){
        this.marks=marks;
    }

    @Override
    public String toString(){
        return "Student{"+"id="+ id + "'name=" +name+ '\''+ "email='"+email+'\''+ "Course='"+ course + '\''+ "marks='"+marks + '}';
    }
}
