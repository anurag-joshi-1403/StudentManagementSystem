package com.anurag.sms.menu;

import java.util.List;
import java.util.Scanner;

import com.anurag.sms.model.Student;
import com.anurag.sms.service.StudentService;

public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private StudentService service = new StudentService();

    public void start() {
        while (true) {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student :");
            System.out.println("2.View All Students :");
            System.out.println("3.Search Student By ID :");
            System.out.println("4. Update Student : ");
            System.out.println("5. Delete Student : ");
            System.out.println("6. Search Student By Name : ");
            System.out.println("7. Search Student By Course : ");
            System.out.println("8. Exit");

            System.out.print("Enter Choice : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    searchStudentById();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    searchStudentsByName();
                    break;
                case 7:
                    searchStudentsByCourse();
                    break;
                case 8:
                    searchStudentsByMarks();
                    break;
                case 9:
                    System.out.println("Thank You...");
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

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("\n===== Student List =====");

        for (Student student : students) {
            System.out.println(student);
        }
    }

    private void searchStudentById() {

        System.out.print("Enter Student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Student student = service.getStudentById(id);

        if (student != null) {
            System.out.println(student);
        } else {
            System.out.println("Student Not Found");
        }
    }

    private void updateStudent() {

        System.out.println("====== Update Student ======");

        System.out.print("Enter Student ID : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter New Name : ");
        String name = scanner.nextLine();

        System.out.print("Enter New Email : ");
        String email = scanner.nextLine();

        System.out.print("Enter New Course : ");
        String course = scanner.nextLine();

        System.out.print("Enter New Marks : ");
        double marks = scanner.nextDouble();
        scanner.nextLine();

        Student student = new Student(id, name, email, course, marks);

        boolean status = service.updateStudent(student);

        if (status) {
            System.out.println("Student Updated Successfully.");
        } else {
            System.out.println("Failed to Update Successfully.");
        }

    }

    private void deleteStudent() {
        System.out.println("Delete Student : ");
        System.out.print("Enter Student Id : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean status = service.deleteStudent(id);

        if (status) {
            System.out.println("Student Deleted Successfully.");
        } else {
            System.out.println("Student Not Found.");
        }

    }

    private void searchStudentsByName() {
        System.out.println("====== Search Student By Name ======");
        System.out.print("Enter Student Name : ");
        String name = scanner.nextLine();

        List<Student> students = service.getStudentsByName(name);

        if (students.isEmpty()) {
            System.out.println("No Student Found");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }

    }

    private void searchStudentsByCourse() {
        System.out.println("====== Search Student By Course ======");
        System.out.print("Enter Student Course : ");
        String course = scanner.nextLine();

        List<Student> students = service.getStudentsByCourse(course);

        if (students.isEmpty()) {
            System.out.println("No Student Found");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }

    }

    private void searchStudentsByMarks() {
        System.out.println("====== Search Student By Marks ======");
        System.out.println("Enter Student Marks : ");
        int marks = scanner.nextInt();

        L

    }

}
