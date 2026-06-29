# JDBC Concepts in Student Management System

## 1. Project Analysis

Your Student Management System uses Java to connect with a MySQL database through JDBC.

The main flow is:

1. The Java program takes student input from the console.
2. The DAO layer prepares a SQL query.
3. JDBC connects Java to the MySQL database.
4. The query is executed.
5. The result is returned to the Java application.

This connection is handled in the files:

- [src/main/java/com/anurag/sms/config/DBConnection.java](../src/main/java/com/anurag/sms/config/DBConnection.java)
- [src/main/java/com/anurag/sms/dao/StudentDAO.java](../src/main/java/com/anurag/sms/dao/StudentDAO.java)

## 2. What is JDBC?

JDBC stands for Java Database Connectivity.

It is a Java API used to connect Java applications with databases like MySQL, Oracle, and PostgreSQL.

In simple words, JDBC acts as a bridge between:

- your Java program, and
- your database system.

## 3. Why JDBC is Used in This Project

This project stores student data in a MySQL database. JDBC helps the Java application to:

- connect to the database,
- send SQL queries,
- insert student records,
- read student records,
- and handle database errors.

## 4. Main JDBC Components

### Connection
A Connection object represents a link between Java and the database.

Example:

```java
Connection connection = DriverManager.getConnection(url, user, password);
```

In your project, this is created inside the DBConnection class.

### Statement / PreparedStatement
These are used to send SQL queries to the database.

Your project uses PreparedStatement, which is safer and more efficient than simple Statement.

Example:

```java
PreparedStatement ps = connection.prepareStatement(sql);
```

### ResultSet
A ResultSet stores the data returned by a SELECT query.

Example:

```java
ResultSet rs = ps.executeQuery();
```

## 5. JDBC Flow in Your Project

### Step 1: Create a Connection
The DBConnection class creates a connection using the MySQL URL, username, and password.

```java
DriverManager.getConnection(URL, USER, PASSWORD);
```

### Step 2: Prepare SQL Query
The DAO class creates an SQL statement to insert or read students.

Example for insert:

```java
String sql = "INSERT INTO students(name, email, course, marks) VALUES(?, ?, ?, ?)";
```

### Step 3: Set Values
The values from Java objects are passed safely into the query.

```java
ps.setString(1, student.getName());
ps.setString(2, student.getEmail());
ps.setString(3, student.getCourse());
ps.setDouble(4, student.getMarks());
```

### Step 4: Execute Query
The query is run using:

```java
ps.executeUpdate();
```

For reading data:

```java
ps.executeQuery();
```

### Step 5: Process Results
If the query returns data, JDBC stores it in a ResultSet and Java processes it.

## 6. JDBC Example Used in This Project

### Insert Example
When a student is added, JDBC executes an insert query:

```java
String sql = "INSERT INTO students(name, email, course, marks) VALUES(?, ?, ?, ?)";
PreparedStatement ps = connection.prepareStatement(sql);
ps.setString(1, student.getName());
ps.setString(2, student.getEmail());
ps.setString(3, student.getCourse());
ps.setDouble(4, student.getMarks());
int rows = ps.executeUpdate();
```

### Select Example
When all students are viewed, JDBC executes a select query:

```java
String sql = "SELECT * FROM students";
PreparedStatement ps = connection.prepareStatement(sql);
ResultSet rs = ps.executeQuery();
```

## 7. Difference Between Statement and PreparedStatement

### Statement
- Simple to use
- Less secure
- Not recommended for user input

### PreparedStatement
- Safer
- Prevents SQL injection
- Better for dynamic values

Your project uses PreparedStatement, which is the better choice.

## 8. Why PreparedStatement is Better

PreparedStatement helps because:

- it prevents SQL injection,
- it separates SQL logic from input values,
- and it is cleaner for dynamic data.

## 9. JDBC Exception Handling

Database operations can fail due to:

- wrong username or password,
- missing database,
- incorrect URL,
- or connection issues.

In your project, exceptions are caught using SQLException.

Example:

```java
catch (SQLException e) {
    e.printStackTrace();
}
```

## 10. JDBC and MVC / Layered Architecture

In your project, JDBC is used in the DAO layer, which is part of the data access layer.

That means:

- the Menu class handles user interaction,
- the Service class handles logic,
- the DAO class handles database operations using JDBC.

This separation keeps the code organized.

## 11. Summary

JDBC is the Java API used to connect Java applications to databases.

In your Student Management System:

- DBConnection creates the database connection.
- StudentDAO uses JDBC to insert and fetch student records.
- PreparedStatement is used to safely execute SQL queries.
- ResultSet stores the data returned from the database.

## 12. Key Takeaway

A simple way to remember JDBC is:

- Connection = connect to database
- PreparedStatement = send SQL query
- ResultSet = read returned data
- SQLException = handle database errors

These are the core building blocks of database interaction in your project.
