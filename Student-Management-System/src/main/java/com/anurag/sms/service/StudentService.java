package com.anurag.sms.service;

import java.util.List;

import com.anurag.sms.dao.StudentDAO;
import com.anurag.sms.model.Student;

public class StudentService {
    private StudentDAO dao = new StudentDAO();

    public boolean addStudent(Student student){
        return dao.addStudent(student);

    }

    public List<Student> getAllStudents(){
        return dao.getAllStudents();
    }

    public Student getStudentById(int id){
        return dao.getStudentById(id);
    }

    public boolean updateStudent(Student student){
        return dao.updateStudent(student);
    }

    public boolean deleteStudent(int id){
        return dao.deleteStudent(id);
    }

    public List<Student> getStudentsByName(String name){
        return dao.getStudentsByName(name);
    }

    public List<Student> getStudentsByCourse(String course){
        return dao.getStudentsByCourse(course);
    }

    public List<Student> getStudentsSortedByMarks(){
        return dao.getStudentsSortedByMarks();
    }

    public int getTotalStudents(){
        return dao.getTotalStudents();
    }
}
