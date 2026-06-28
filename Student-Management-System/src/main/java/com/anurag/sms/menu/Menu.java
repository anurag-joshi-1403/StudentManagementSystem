package com.anurag.sms.menu;

import java.util.Scanner;

import com.anurag.sms.model.Student;
import com.anurag.sms.service.StudentService;

public class Menu{

    private Scanner scanner = new Scanner(System.in);
    private StudentService service = new StudentService();

    public void start(){
        System.out.println("===== Add Student =====");

        System.out.print("Enter Name : ");
        String name = scanner.nextLine();

        System.out.print("Enter Email : ");
        String email = scanner.nextLine();

        System.out.print("Enter Course : ");
        String course = scanner.nextLine();

        System.out.print("Enter Marks : ");
        double marks = scanner.nextDouble();

        Student student = new Student(name, email, course, marks);

        boolean status = service.addStudent(student);

        if(status){
            System.out.println("Student Added Successfully. ");
        }else {
            System.out.println("Failed to Add Student.");
        }
    }
}