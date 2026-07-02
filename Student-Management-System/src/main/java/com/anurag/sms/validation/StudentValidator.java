package com.anurag.sms.validation;

public class StudentValidator {
    // /Validate Name
    public static boolean validateName(String name) {
        if(name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("[A-Za-z]+( [A-Za-z]+)*");
    }

    // Validate Email
    public static boolean validateEmail(String email){
        if(email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Validate Course
    public static boolean validateCourse(String course){
        if(course == null || course.trim().isEmpty()){
            return false;
        }
        return true;
    }

    // Validate Marks
    public static boolean validateMarks(double marks) {
        return marks >= 0 && marks <= 100;
    }
}
