# 🚀 Student Management System

A Java-based console application for managing student records using MySQL, JDBC, and Maven.

## 🛠️ Project Flow From Scratch to Now

This project started as a beginner-friendly Java console app and has grown into a small layered application with:

- a console-based menu,
- a Student model class,
- a service layer for business logic,
- a DAO layer for database operations,
- MySQL database connectivity through JDBC,
- and complete CRUD-style functionality.

## ✨ Technical Stickers & Highlights

- 🧠 OOP Concepts: classes, objects, constructors, and encapsulation
- 🗄️ Database Connectivity: MySQL + JDBC integration
- 🧩 Layered Architecture: menu, service, DAO, and model separation
- 🔐 Secure Queries: PreparedStatement for safe SQL execution
- 📊 Data Insights: statistics like total, highest, lowest, and average marks
- 🧪 Build & Run: Maven-based project structure with Java execution

## 🎯 What the Project Does Now

The application allows a user to:

1. Add a new student
2. View all students
3. Search a student by ID
4. Update an existing student
5. Delete a student
6. Search students by name
7. Search students by course
8. Sort students by marks
9. View student statistics such as total count, highest marks, lowest marks, and average marks
10. Exit the program

## 🧰 Tech Stack

- ☕ Java
- 🧱 Maven
- 🐬 MySQL
- 🔌 JDBC
- 🧪 JUnit 5 (for testing support)
- ⌨️ Scanner for console input

## Project Structure

- src/main/java/com/anurag/sms/config/ - database connection class
- src/main/java/com/anurag/sms/dao/ - database access layer
- src/main/java/com/anurag/sms/model/ - student model
- src/main/java/com/anurag/sms/service/ - business logic layer
- src/main/java/com/anurag/sms/menu/ - console UI and main entry point
- database/ - SQL scripts for database setup
- docs/ - project notes and documentation

## 🔄 Current Application Flow

1. The program starts from the main class.
2. The menu is displayed in the console.
3. The user selects an option.
4. The request is passed to the service layer.
5. The service layer calls the DAO layer.
6. The DAO sends SQL statements to the MySQL database.
7. The result is returned to the menu and printed to the console.

## Core Classes

- Main: starts the application
- Menu: handles console interaction and user choices
- StudentService: manages business actions and calls the DAO
- StudentDAO: performs database operations using SQL
- Student: represents a student entity
- DBConnection: establishes the database connection

## New Terms Used in This Project

### 1. Layered Architecture
Layered architecture means dividing the project into separate layers so each part has one job.

- Presentation layer: the Menu class handles user interaction.
- Service layer: the StudentService class contains the business logic.
- DAO layer: the StudentDAO class handles database work.
- Model layer: the Student class represents the student data.

This design makes the project easier to understand, maintain, and expand.

### 2. Model Class
A model class is a blueprint for an object.

In this project, the Student class is the model. It contains fields such as:

- id
- name
- email
- course
- marks

It also provides getters and setters so data can be stored and accessed easily.

### 3. DAO (Data Access Object)
DAO stands for Data Access Object. It is the class that talks directly with the database.

The StudentDAO class contains methods such as:

- addStudent()
- getAllStudents()
- getStudentById()
- updateStudent()
- deleteStudent()
- getStudentsByName()
- getStudentsByCourse()
- getStudentsSortedByMarks()

Its job is to run SQL queries and return results.

### 4. Service Layer
The service layer sits between the menu and the DAO.

It receives requests from the user interface and passes them to the DAO. It helps keep the code organized and separates business logic from database logic.

### 5. JDBC
JDBC stands for Java Database Connectivity. It is the API used in Java to connect to a database.

In this project, JDBC is used to:

- create a connection to MySQL,
- send SQL commands,
- receive query results,
- and update data in the database.

### 6. Connection
A Connection object represents the link between the Java application and the MySQL database.

Before running any SQL query, the program opens a connection. After the work is complete, it closes the connection to save resources.

### 7. PreparedStatement
PreparedStatement is a safer way to execute SQL queries in Java.

Instead of directly inserting values into the SQL string, the code uses placeholders like ? and binds values separately.

This helps with:

- security,
- readability,
- and preventing SQL injection.

Example:

```java
String sql = "INSERT INTO students(name, email, course, marks) VALUES(?, ?, ?, ?)";
```

### 8. ResultSet
A ResultSet is an object that stores the output of a SELECT query.

When the program reads student records from the database, the data is stored in a ResultSet, and the code moves through it row by row.

### 9. CRUD
CRUD stands for Create, Read, Update, and Delete.

This project supports all four operations:

- Create: add a student
- Read: view students and search them
- Update: edit student details
- Delete: remove a student

### 10. SQL Keywords and Clauses
These are important SQL terms used in the project:

- SELECT: fetch data from the database
- INSERT: add new data
- UPDATE: change existing data
- DELETE: remove data
- WHERE: filter rows based on a condition
- LIKE: search for partial matches
- ORDER BY: sort rows

### 11. LIKE Operator and Wildcards
The LIKE operator is used for partial matching.

In this project, it is used to search students by name or course.

Example:

```sql
SELECT * FROM students WHERE name LIKE ?
```

The % symbol is a wildcard that means “any number of characters”.

### 12. Aggregation Functions
Aggregation functions calculate summary values from the data.

The project uses:

- COUNT(): count total students
- MAX(): find highest marks
- MIN(): find lowest marks
- AVG(): calculate average marks

### 13. Primary Key and Auto-Increment
A primary key uniquely identifies each row in a table.

In the students table, the id column is used as the primary key. MySQL can also auto-generate this value using AUTO_INCREMENT.

This means each new student gets a unique ID automatically.

### 14. Exception Handling
Exception handling is used to manage errors when database operations fail.

In this project, SQL exceptions are caught and printed so the program can respond safely instead of crashing.

## 📅 Day-wise Progress (Based on the Current Codebase)

### Day 1 - Project Setup
- Created a Maven-based Java project.
- Added MySQL and JDBC dependencies.
- Set up the basic folder structure for the application.

### Day 2 - Database and Model Creation
- Created the Student model class.
- Built the database connection class.
- Prepared the project to connect with MySQL.

### Day 3 - Core Java Layering
- Added the Menu class for user interaction.
- Implemented the StudentService layer for business logic.
- Created the StudentDAO layer for database operations.

### Day 4 - CRUD Functionality
- Implemented adding students.
- Implemented viewing all students.
- Added student search by ID.
- Added update and delete operations.

### Day 5 - Advanced Operations
- Added search by name and course using LIKE.
- Added sorting by marks using ORDER BY.
- Added student statistics such as total, highest, lowest, and average marks.

### Day 6 - Documentation and Learning
- Wrote project explanations and SQL notes.
- Documented the flow of the application.
- Explained new technical terms used in the project.

## Database Setup

Before running the project, make sure MySQL is installed and running.

Create a database named:

```sql
student_management
```

You can use the SQL files located in the database folder to prepare the schema and sample data.

## Configuration

The database connection details are currently stored in:

- src/main/resources/db.properties
- src/main/java/com/anurag/sms/config/DBConnection.java

Make sure the database credentials match your local MySQL setup.

## How to Run

From the project root, run:

```bash
mvn clean compile
mvn exec:java
```

If the application starts successfully, you will see the Student Management System menu in the terminal.

## ✅ Current Status

The project is currently in a working intermediate stage with:

- menu-driven student entry,
- student listing,
- CRUD operations,
- name and course search,
- marks sorting,
- student statistics,
- and a layered Java architecture.

## Future Improvements

Possible next steps for the project:

- add input validation,
- improve error handling,
- use properties-based configuration more securely,
- add unit tests,
- build a GUI or web version,
- add pagination and filtering features.

## Summary

This project demonstrates a practical Java application flow using:

- console input/output,
- object-oriented programming,
- layered architecture,
- JDBC database operations,
- SQL queries and prepared statements,
- and Maven project structure.
