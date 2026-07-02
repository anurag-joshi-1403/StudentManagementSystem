package com.anurag.sms.validation;

public class StudentValidation {
    // /Validate Name
    public static boolean validateName(String name) {
        if(name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("[A-Za-z]{3,50}");
    }

    // Validate Email
    public static boolean validateEmail(String email){
        if(email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    
}
