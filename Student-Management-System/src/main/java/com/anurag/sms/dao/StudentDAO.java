package com.anurag.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.anurag.sms.config.DBConnection;
import com.anurag.sms.model.Student;

public class StudentDAO {

    public boolean updateStudent(Student student){
            String sql = "UPDATE students SET name=?, email=?, course=?, marks=? WHERE id=?";

            try{
                Connection connection = DBConnection.getConnection();

                PreparedStatement ps = connection.prepareStatement(sql);

                ps.setString(1, student.getName());
                ps.setString(2, student.getEmail());
                ps.setString(3, student.getCourse());
                ps.setDouble(4, student.getMarks());
                ps.setInt(5, student.getId());

                int rows = ps.executeUpdate();

                connection.close();

                return rows > 0;
            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }

    public boolean addStudent(Student student) {
        String sql = """
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

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students";
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getDouble("marks"));
                students.add(student);

            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Search Student by Id
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try {
            Connection connection = DBConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getDouble("marks")
                );
                connection.close();
                return student;
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id=?";

        try{
            Connection connection = DBConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            connection.close();

            return rows > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Student> getStudentsByName(String name){

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students WHERE name Like ?";

        try{
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, "%" + name + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Student student = new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("course"),
                    rs.getDouble("marks")
                );
                students.add(student);
            }
            connection.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return students;
    }

    public List<Student> getStudentsByCourse(String course){

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students WHERE course Like ?";

        try{
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, "%" + course + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Student student = new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("course"),
                    rs.getDouble("marks")
                );
                students.add(student);
            }
            connection.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return students;
    }

    public List<Student> getAtudentsSortedByMarks(int marks){

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students ORDER BY marks DESC";

        try{
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, "%" + marks + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Student student = new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("course"),
                    rs.getDouble("marks")
                );
                students.add(student);
            }
            connection.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return students;
    }

}

