package com.anurag.sms.service;

import java.util.List;

import com.anurag.sms.dao.StudentDAO;
import com.anurag.sms.exception.StudentNotFoundException;
import com.anurag.sms.model.Student;

/*
* Service class that provides business logic 
* for student Management System
*
* @author Anurag Joshi
*/
public class StudentService {
    private StudentDAO dao = new StudentDAO();

    /*
    *Adds a student
    *
    * @param student Student object
    * @return true if student is added successfully, otherwise false
    */
    public boolean addStudent(Student student){
        return dao.addStudent(student);

    }

    /*
    * Retrieves all students.
    *
    * @return list of student
    */
    public List<Student> getAllStudents(){
        return dao.getAllStudents();
    }

    /*
    * Retrieves a student using student ID.
    *
    * @param id student ID
    * @return Student object
    * @throws StudentNotFoundException if the student is not found 
    */
    public Student getStudentById(int id) throws StudentNotFoundException {
        Student student = dao.getStudentById(id);
        if(student == null) {
            throw new StudentNotFoundException("Student with id" + id + " not found.");
        }
        return student;
    }

    /*
    * Updates a student's details
    *
    * @param student Student object
    * @return true if updated successfully, otherwise false
    */
    public boolean updateStudent(Student student){
        return dao.updateStudent(student);
    }

    /*
    * Delete a student
    *
    * @param id student ID
    * @return true if deleted successfully, otherwise false
    */
    public boolean deleteStudent(int id){
        return dao.deleteStudent(id);
    }

    /*
    * Search students by name
    *
    * @param name student name
    * @return list of matching students
    */
    public List<Student> getStudentsByName(String name){
        return dao.getStudentsByName(name);
    }

    /*
    * Retrieves students by course
    *
    * @param course name
    * @return list of students
    */
    public List<Student> getStudentsByCourse(String course){
        return dao.getStudentsByCourse(course);
    }

    /*
    * Retrieves student sorted by marks
    *
    * @return sorted list of students
    */
    public List<Student> getStudentsSortedByMarks(){
        return dao.getStudentsSortedByMarks();
    }

    /*
    * Returns total number of student count
    *
    * @return total student count
    */
    public int getTotalStudents(){
        return dao.getTotalStudents();
    }

    /*
    * Returns the highest marks
    *
    * @return highest marks
    */
    public double  getHighMarks(){
        return dao.getHighMarks();
    }

    /*
    * Returns the lowest marks 
    *
    * @return lowest marks
    */
    public double  getLowestMarks(){
        return dao.getLowestMarks();
    }
    
    /*
    * Returns the average marks
    *
    * @return average marks
    */
    public double  getAverageMarks(){
        return dao.getAverageMarks();
    }

    /*
    * Exports student data to a CSV file.
    *
    * @return true if export is successful, otherwise false
    */
    public boolean exportStudentsToCSV() {
        return dao.exportStudentsToCSV();
    }

}
