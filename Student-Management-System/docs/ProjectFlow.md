# Student Management System

A simple Java-based console application for managing student records using MySQL.

## Project Flow From Scratch to Now

This project was built step by step as a beginner-friendly Java application with:

- a console-based menu,
- a student model,
- service and DAO layers,
- MySQL database connectivity,
- and basic CRUD-style functionality.

## What the Project Does

The application allows a user to:

1. Add a new student
2. View all students
3. Exit the program

## Tech Stack

- Java
- Maven
- MySQL
- JDBC
- JUnit 5 (for testing support)

## Project Structure

- src/main/java/com/anurag/sms/config/ - database connection class
- src/main/java/com/anurag/sms/dao/ - database access layer
- src/main/java/com/anurag/sms/model/ - student model
- src/main/java/com/anurag/sms/service/ - business logic layer
- src/main/java/com/anurag/sms/menu/ - console UI and main entry point
- database/ - SQL scripts for database setup
- docs/ - project notes and documentation

## Current Application Flow

1. The program starts from the main class.
2. The menu is displayed in the console.
3. The user selects an option.
4. If the user chooses to add a student, the input is collected and sent to the service layer.
5. The DAO inserts the student into the MySQL database.
6. If the user chooses to view students, the DAO retrieves records and displays them.

## Core Classes

- Main: starts the application
- Menu: handles console interaction
- StudentService: manages business actions
- StudentDAO: performs database operations
- Student: represents a student entity
- DBConnection: establishes the database connection

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

## Current Status

The project is currently in a basic working stage with:

- menu-driven student entry,
- student listing,
- MySQL database integration,
- and a simple layered Java architecture.

## Future Improvements

Possible next steps for the project:

- add update and delete functionality,
- add input validation,
- improve error handling,
- use properties-based configuration securely,
- add unit tests,
- build a GUI or web version.

## Summary

This project demonstrates a practical beginner-to-intermediate Java application flow using:

- console input/output,
- object-oriented programming,
- layered architecture,
- JDBC database operations,
- and Maven project structure.
