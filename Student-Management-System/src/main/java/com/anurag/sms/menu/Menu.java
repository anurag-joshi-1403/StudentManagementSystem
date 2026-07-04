package com.anurag.sms.menu;

import java.util.List;
import java.util.Scanner;

import com.anurag.sms.exception.StudentNotFoundException;
import com.anurag.sms.model.Student;
import com.anurag.sms.service.StudentService;
import com.anurag.sms.validation.StudentValidator;

/**
 * Provides the console-based menu for the
 * Student Management System.
 *
 * Handles user interaction and calls the
 * service layer methods.
 *
 * @author Anurag Joshi
 */
public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private StudentService service = new StudentService();

    /**
     * Starts the application and displays the
     * main menu repeatedly until the user exits.
     */
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
            System.out.println("8. Search Student Sorted By Marks : ");
            System.out.println("9. Show Student Statistics");
            System.out.println("10. Export Student to CSV : ");
            System.out.println("11. Exit");

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
                    showStudentStatistics();
                    break;
                case 10:
                    exportStudentsToCSV();
                    break;
                case 11:
                    System.out.println("Thank You...");
                    return;
                default:
                    System.out.println("Invalid Choice");
            }

        }
    }

    /**
     * Accepts student details from the user,
     * validates the input, and adds the student
     * to the database.
     */
    private void addStudent() {
        System.out.println("===== Add Student =====");

        System.out.print("Enter Name : ");
        String name = scanner.nextLine();
        if (!StudentValidator.validateName(name)) {
            System.out.println("Invalid Name!");
            return;
        }

        System.out.print("Enter Email : ");
        String email = scanner.nextLine();
        if (!StudentValidator.validateEmail(email)) {
            System.out.println("Invalid Email!");
            return;
        }

        System.out.print("Enter Course : ");
        String course = scanner.nextLine();
        if (!StudentValidator.validateCourse(course)) {
            System.out.println("Course cannot be empty!");
            return;
        }

        System.out.print("Enter Marks : ");
        double marks = scanner.nextDouble();
        scanner.nextLine();
        if (!StudentValidator.validateMarks(marks)) {
            System.out.println("Marks must be between 0 and 100!");
            return;
        }

        Student student = new Student(name, email, course, marks);

        boolean status = service.addStudent(student);

        if (status) {
            System.out.println("Student Added Successfully. ");
        } else {
            System.out.println("Failed to Add Student.");
        }

    }

    /**
     * Displays all students available in the database.
     */
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

    /**
     * Searches for a student using the student ID
     * entered by the user.
     */
    private void searchStudentById() {

        System.out.print("Enter Student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        try {
            Student student = service.getStudentById(id);
            System.out.println(student);
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Updates the details of an existing student.
     */
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

    /**
     * Deletes a student using the entered ID.
     */

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

    /**
     * Displays students whose names match the
     * entered name.
     */

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

    /**
     * Displays students enrolled in the entered course.
     */
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

    /**
     * Displays students sorted according to marks.
     */
    private void searchStudentsByMarks() {
        System.out.println("====== Search Student By Marks ======");

        List<Student> students = service.getStudentsSortedByMarks();

        if (students.isEmpty()) {
            System.out.println("No Student Found");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }

    }

    /**
     * Displays student statistics such as total students,
     * highest marks, lowest marks, and average marks.
     */
    // Show Student Statistics
    private void showStudentStatistics() {
        System.out.println("/n====== Student Statistics ======");

        int total = service.getTotalStudents();
        double highest = service.getHighMarks();
        double lowest = service.getLowestMarks();
        double average = service.getAverageMarks();

        System.out.println("Total Student : " + total);
        System.out.println("Highest Marks : " + highest);
        System.out.println("Lowest Marks : " + lowest);
        System.out.println("Average Marks : " + average);

        System.out.println("=================================");
    }

    /**
     * Exports all student records to a CSV file.
     */
    private void exportStudentsToCSV() {
        System.out.println("====== Export Students To CSV ====== ");

        boolean status = service.exportStudentsToCSV();

        if (status) {
            System.out.println("Students Exported Successfully.");
        } else {
            System.out.println("Export Failed");
        }
    }

}
