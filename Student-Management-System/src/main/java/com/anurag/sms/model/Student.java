package com.anurag.sms.model;


/*
* Represents a student.
*/
public class Student {
    private int id;
    private String name;
    private String email;
    private String course;
    private double  marks;

    /*
    * Create an empty Student object.
    */
    // Default Constructor
    public Student(){
    }

    /*
    * Create a student without an ID.
    * @param name Student name
    * @param email Student email
    * @param course Student course
    * @param marks Student marks

    */

    // Constructor without ID
    public Student(String name, String email, String course, double marks){
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    /*
    *Create a Student with all details.
    *
    *  @param id Student ID
    *  @param name Student name
    *  @param email Student email
    *  @param course Student course
    *  @param marks Student marks
    */
    // Constructor with ID
    public Student(int id, String name, String email, String course, double marks){
        this.id=id;
        this.name= name;
        this.email= email;
        this.course=course;
        this.marks = marks;
    }

    /*
    *Returns the student id
    *
    * @return student id
    */
    public int getId(){
        return id;
    }
    /*
    *Sets the student id
    *
    * @return student id
    */
    public void setId(int id){
        this.id=id;
    }

    /*
    *Returns the student name
    *
    * @return student name
    */
    public String getName(){
        return name;
    }
    /*
    *Sets the student name
    *
    * @return student name
    */
    public void setName(String name){
        this.name=name;
    }

    /*
    *Returns the student email
    *
    * @return student email
    */
    public String getEmail(){
        return email;
    }
    /*
    *Sets the student email
    *
    * @return student email
    */
    public void setEmail(String email){
        this.email=email;
    }
    /*
    *Returns the student course
    *
    * @return student course
    */
    public String getCourse(){
        return course;
    }
    /*
    *Sets the student course
    *
    * @return student course
    */
    public void setCourse(String course){
        this.course=course;
    }

    /*
    *Returns the student marks 
    *
    * @return student marks
    */
    public double getMarks(){
        return marks;
    }
    /*
    *Sets the student marks
    *
    * @return student marks
    */
    public void setMarks(double marks){
        this.marks=marks;
    }

    /*
    *Returns a String representation of the student.
    *
    * @return formatted student details
    */
    @Override
    public String toString(){
        return "Student{"+"id="+ id + "'name=" +name+ '\''+ "email='"+email+'\''+ "Course='"+ course + '\''+ "marks='"+marks + '}';
    }
}
