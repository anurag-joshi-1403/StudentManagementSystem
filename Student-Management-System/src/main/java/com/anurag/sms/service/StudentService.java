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
}
