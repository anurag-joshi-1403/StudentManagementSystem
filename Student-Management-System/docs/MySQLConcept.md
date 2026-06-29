# MySQL Concepts in Student Management System

## 1. Project Analysis

The Student Management System is a Java console application that stores and retrieves student data from a MySQL database.

The flow of the application is:

1. The user runs the program from the main class.
2. The menu collects student information.
3. The service layer processes the request.
4. The DAO layer sends SQL commands to the database.
5. The database stores or returns the student records.

In this project, SQL is mainly used in the DAO layer, especially in the file [src/main/java/com/anurag/sms/dao/StudentDAO.java](../src/main/java/com/anurag/sms/dao/StudentDAO.java).

## 2. How SQL is Used in This Project

### Insert Operation
The application inserts new student data into the database using SQL:

```sql
INSERT INTO students(name, email, course, marks)
VALUES (?, ?, ?, ?);
```

This is used when a user adds a student through the console menu.

### Select Operation
The application reads all students from the database using:

```sql
SELECT * FROM students;
```

This is used when the user wants to view all stored students.

## 3. Basic SQL Concepts

### Database
A database is a structured place where data is stored.

In this project, the database name is:

```sql
student_management
```

### Table
A table stores related data in rows and columns.

For this project, the main table is expected to be:

```sql
students
```

A typical students table may look like this:

```sql
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100),
    course VARCHAR(100),
    marks DOUBLE
);
```

### Row and Column
- A row = one record, such as one student.
- A column = one field, such as name, email, course, or marks.

## 4. CRUD Operations

SQL is commonly used for CRUD operations:

- Create: insert new data
- Read: fetch existing data
- Update: modify data
- Delete: remove data

### Create
```sql
INSERT INTO students(name, email, course, marks)
VALUES ('Aman', 'aman@example.com', 'Java', 85.5);
```

### Read
```sql
SELECT * FROM students;
```

### Update
```sql
UPDATE students
SET marks = 90
WHERE id = 1;
```

### Delete
```sql
DELETE FROM students
WHERE id = 1;
```

## 5. Important SQL Keywords

### SELECT
Used to fetch data.

```sql
SELECT name, email FROM students;
```

### INSERT
Used to add new records.

```sql
INSERT INTO students(name, email, course, marks)
VALUES ('Riya', 'riya@example.com', 'Python', 78);
```

### UPDATE
Used to change existing records.

```sql
UPDATE students
SET course = 'Spring Boot'
WHERE id = 2;
```

### DELETE
Used to remove records.

```sql
DELETE FROM students
WHERE id = 2;
```

## 6. WHERE Clause
The WHERE clause filters rows based on a condition.

```sql
SELECT * FROM students
WHERE course = 'Java';
```

This helps find only the students matching a specific condition.

## 7. ORDER BY Clause
The ORDER BY clause sorts records.

```sql
SELECT * FROM students
ORDER BY marks DESC;
```

This shows highest marks first.

## 8. Primary Key
A primary key uniquely identifies each row in a table.

In the students table, the id column is usually the primary key.

```sql
id INT PRIMARY KEY AUTO_INCREMENT
```

This means:
- every student has a unique id,
- and the database automatically assigns it.

## 9. Prepared Statements in Java

In this project, the SQL statements are executed using JDBC and PreparedStatement.

This is better than writing raw SQL directly because it:

- prevents SQL injection,
- improves readability,
- and safely inserts user input.

Example from the project:

```java
PreparedStatement ps = connection.prepareStatement(sql);
ps.setString(1, student.getName());
```

## 10. Relationship Between Java and SQL

The Java application does not directly store data itself. Instead, it sends SQL commands to the MySQL server.

The relationship is:

- Java collects input from the user
- Java sends SQL commands to the database
- MySQL stores or returns the data
- Java displays the result to the user

## 11. Summary

SQL is the language used to communicate with the database in this project.

In your Student Management System:
- INSERT is used to add students
- SELECT is used to view all students
- MySQL stores the data in a table named students
- JDBC connects Java to the database
- PreparedStatement helps run SQL safely

## 12. Key Takeaway

A simple way to remember SQL is:

- CREATE = make a table
- INSERT = add data
- SELECT = read data
- UPDATE = modify data
- DELETE = remove data

These five operations form the foundation of database handling in this project.
