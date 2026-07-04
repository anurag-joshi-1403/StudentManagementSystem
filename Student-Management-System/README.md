# 🎓 Student Management System

![Java](https://img.shields.io/badge/Java-17-orange)
![Maven](https://img.shields.io/badge/Maven-3.9-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![JDBC](https://img.shields.io/badge/JDBC-Database-green)
![Status](https://img.shields.io/badge/Project-Completed-brightgreen)
![License](https://img.shields.io/badge/License-MIT-yellow)

A **console-based Student Management System** developed using **Java, JDBC, MySQL, and Maven** following a layered architecture (Menu → Service → DAO → Database).

This project demonstrates **Core Java**, **Object-Oriented Programming**, **JDBC**, **SQL**, **Exception Handling**, **Input Validation**, **CSV Export**, **Logging**, and **Maven** in a real-world CRUD application.

---

# 📖 Table of Contents

- Project Overview
- Features
- Technologies Used
- Project Architecture
- Project Structure
- Database Design
- Application Flow
- Installation
- Database Setup
- How to Run
- Sample Menu
- Exception Handling
- Validation Rules
- CSV Export
- Logging
- Future Enhancements
- Author

---

# 📌 Project Overview

The Student Management System helps manage student records from the command line.

Users can:

- Add Students
- View Students
- Search Students
- Update Records
- Delete Records
- Display Statistics
- Export Data to CSV

The application stores all data inside a MySQL database using JDBC.

---

# ✨ Features

## Student Operations

✅ Add Student

✅ View All Students

✅ Search Student by ID

✅ Search Student by Name

✅ Search Student by Course

✅ View Students Sorted by Marks

✅ Update Student

✅ Delete Student

---

## Statistics

✅ Total Students

✅ Highest Marks

✅ Lowest Marks

✅ Average Marks

---

## Validation

✅ Name Validation

✅ Email Validation

✅ Course Validation

✅ Marks Validation

---

## Exception Handling

✅ StudentNotFoundException

✅ InvalidStudentDataException

---

## Additional Features

✅ CSV Export

✅ Java Logging

✅ Maven Build

---

# 🛠 Technologies Used

| Technology | Version |
|------------|----------|
| Java | 17 |
| Maven | 3.x |
| MySQL | 8.x |
| JDBC | MySQL Connector |
| VS Code | Latest |
| Git | Latest |
| GitHub | Repository Hosting |

---

# 🏗 Project Architecture

```
             User

              │

              ▼

        Menu (UI Layer)

              │

              ▼

      StudentService

              │

              ▼

        StudentDAO

              │

              ▼

      JDBC Connection

              │

              ▼

        MySQL Database
```

---

# 📁 Project Structure

```
Student-Management-System
│
├── database
│
├── docs
│   ├── CommonErrors.md
│   ├── InterviewQuestion.md
│   ├── JDBCConcept.md
│   ├── MySQLConcept.md
│   └── ProjectFlow.md
│
├── src
│   └── main
│       ├── config
│       ├── dao
│       ├── exception
│       ├── menu
│       ├── model
│       ├── service
│       ├── util
│       ├── validation
│       └── resources
│
├── pom.xml
└── README.md
```

---

# 🗄 Database Design

Table Name

```
students
```

Columns

| Column | Type |
|----------|------|
| id | INT |
| name | VARCHAR |
| email | VARCHAR |
| course | VARCHAR |
| marks | DOUBLE |

---

# 🔄 Application Flow

```
Start Application

        │

        ▼

 Display Main Menu

        │

        ▼

User Chooses Option

        │

        ▼

Validation

        │

        ▼

Service Layer

        │

        ▼

DAO Layer

        │

        ▼

Database

        │

        ▼

Display Result
```

---

# ⚙ Installation

Clone Repository

```bash
git clone <repository-url>
```

Open Project

```
VS Code
```

Install

- Java 17
- Maven
- MySQL

---

# 🛢 Database Setup

Create database

```sql
CREATE DATABASE student_management;
```

Create table

```sql
CREATE TABLE students(
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(100),
email VARCHAR(100),
course VARCHAR(100),
marks DOUBLE
);
```

Configure

```
src/main/resources/db.properties
```

---

# ▶ Running the Project

Compile

```bash
mvn clean compile
```

Run

```bash
mvn exec:java
```

or

Run `Main.java`

---

# 📋 Sample Menu

```
===== Student Management System =====

1. Add Student

2. View All Students

3. Search Student By ID

4. Update Student

5. Delete Student

6. Search Student By Name

7. Search Student By Course

8. Search Student Sorted By Marks

9. Show Student Statistics

10. Export Student To CSV

11. Exit
```

---

# ⚠ Exception Handling

The application uses custom exceptions.

- StudentNotFoundException
- InvalidStudentDataException

These improve error readability and maintainability.

---

# ✅ Validation Rules

| Field | Validation |
|---------|------------|
| Name | Only Alphabets & Spaces |
| Email | Valid Email Format |
| Course | Cannot be Empty |
| Marks | 0 – 100 |

---

# 📄 CSV Export

Student records can be exported into

```
student.csv
```

Generated automatically after selecting

```
10. Export Student To CSV
```

---

# 📝 Logging

The application uses

```
java.util.logging.Logger
```

to log

- Successful Operations

- Database Errors

- Exceptions

---

# 🚀 Future Enhancements

- Login System

- User Authentication

- Role Based Access

- Attendance Module

- Fee Management

- GUI using JavaFX

- REST API

- Spring Boot Migration

- PDF Export

- Excel Export

---

# 👨‍💻 Author

**Anurag Joshi**

Java Developer

GitHub:
(Add your GitHub Profile)

---

# ⭐ Learning Outcomes

This project demonstrates practical knowledge of:

- Core Java

- OOP

- Collections

- Exception Handling

- JDBC

- SQL

- MySQL

- Maven

- File Handling

- CSV Export

- Logging

- Clean Architecture

---

# 📜 License

This project is created for educational purposes and learning Java backend development.