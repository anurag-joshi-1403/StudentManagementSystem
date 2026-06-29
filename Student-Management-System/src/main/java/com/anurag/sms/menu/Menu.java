package com.anurag.sms.menu;

import java.util.List;
import java.util.Scanner;

import com.anurag.sms.model.Student;
import com.anurag.sms.service.StudentService;

public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private StudentService service = new StudentService();

    public void start() {
        while(true){
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2.View All Students");
            System.out.println("3. Exit");

            System.out.print("Enter Choice : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    addStudent();
                    break;
                case 2 :
                    viewAllStudents();
                    break;
                case 3 :
                    System.out.println("Thank You!");
                    return;
                default:
                    System.out.println("Invalid Choice");
            }

        }
    }

    private void addStudent() {
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

        if (status) {
            System.out.println("Student Added Successfully. ");
        } else {
            System.out.println("Failed to Add Student.");
        }

    }

    private void viewAllStudents() {
        List<Student> students = service.getAllStudents();

        if(students.isEmpty()){
            System.out.println("No students found.");
            return;
        }
        System.out.println("\n===== Student List =====");

        for(Student student : students){
            System.out.println(student);
        }
    }

}
