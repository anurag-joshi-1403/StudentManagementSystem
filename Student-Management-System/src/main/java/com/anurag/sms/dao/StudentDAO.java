package com.anurag.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.anurag.sms.config.DBConnection;
import com.anurag.sms.model.Student;

public class StudentDAO {

    public boolean addStudents(Student student) {
        String sql ="""
                INSERT INTO students(name,email,course,marks)VALUES( ?,  ?,  ?,  ?)""";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getCourse());
            ps.setDouble(4, student.getMarks());

            int rows = ps.executeUpdate();

            connection.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
