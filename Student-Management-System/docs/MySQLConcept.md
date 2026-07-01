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

---

## 13. Advanced SQL Terms Used in This Project

### LIKE Operator
The LIKE operator is used for pattern matching in SQL queries. It searches for a specified pattern within a column.

**Syntax:**
```sql
SELECT * FROM students WHERE name LIKE pattern;
```

**In this project:**
```java
String sql = "SELECT * FROM students WHERE name Like ?";
ps.setString(1, "%" + name + "%");
```

The `%` is a wildcard that means "any number of characters". For example:
- `"Aman%"` matches "Aman", "AmandQA", "Amandeep"
- `"%Aman"` matches "Raman", "Aman", "Shaman"
- `"%Aman%"` matches any name containing "Aman" anywhere in it

**Real Example:**
If you search for "a", it will find all students with "a" in their name:
```sql
SELECT * FROM students WHERE name LIKE '%a%';
```
Results: "Aman", "Raman", "Harsha", "Priyanka"

**When used in the project:**
- Used in `getStudentsByName()` method to search students by partial name match
- Used in `getStudentsByCourse()` method to search students by partial course name match

---

### ResultSet
A ResultSet is a Java object that holds the results returned by a database query. It acts like a table with rows and columns that you can navigate through.

**Key Characteristics:**
- It's used with SELECT queries
- You can move through rows one by one
- You can extract column values using methods like `getString()`, `getInt()`, `getDouble()`

**In this project:**
```java
ResultSet rs = ps.executeQuery();

while (rs.next()) {
    Student student = new Student(
        rs.getInt("id"),
        rs.getString("name"),
        rs.getString("email"),
        rs.getString("course"),
        rs.getDouble("marks")
    );
    students.add(student);
}
```

**Common ResultSet Methods:**
- `rs.next()` - moves to the next row, returns true if a row exists
- `rs.getString(columnName)` - gets String value from column
- `rs.getInt(columnName)` - gets Integer value from column
- `rs.getDouble(columnName)` - gets Double value from column
- `rs.getBoolean(columnName)` - gets Boolean value from column

---

### executeQuery() vs executeUpdate()

These are two different methods used to execute SQL statements:

#### executeQuery()
- Used for SELECT queries only
- Returns a **ResultSet** object
- Used to fetch/read data

**Example:**
```java
String sql = "SELECT * FROM students";
ResultSet rs = ps.executeQuery();
```

#### executeUpdate()
- Used for INSERT, UPDATE, DELETE queries
- Returns an **int** (number of rows affected)
- Does NOT return data, returns count of modified rows

**Example:**
```java
String sql = "UPDATE students SET marks=? WHERE id=?";
int rows = ps.executeUpdate();
// rows will be 1 if update was successful
```

**In this project:**
- `executeQuery()` is used in: `getAllStudents()`, `getStudentById()`, `getStudentsByName()`, `getStudentsByCourse()`
- `executeUpdate()` is used in: `addStudent()`, `updateStudent()`, `deleteStudent()`

---

### Parameter Binding and Placeholders

Parameter binding is a secure way to insert user input into SQL queries. Instead of directly concatenating strings, you use placeholders (`?`) and bind values separately.

**Why it's important:**
- Prevents SQL Injection attacks
- Improves performance
- Makes code cleaner and safer

**Syntax:**
```java
String sql = "INSERT INTO students(name, email, course, marks) VALUES (?, ?, ?, ?)";
PreparedStatement ps = connection.prepareStatement(sql);

ps.setString(1, student.getName());      // First ?
ps.setString(2, student.getEmail());     // Second ?
ps.setString(3, student.getCourse());    // Third ?
ps.setDouble(4, student.getMarks());     // Fourth ?

int rows = ps.executeUpdate();
```

**Common Binding Methods:**
- `ps.setString(index, value)` - for VARCHAR columns
- `ps.setInt(index, value)` - for INT columns
- `ps.setDouble(index, value)` - for DOUBLE columns
- `ps.setBoolean(index, value)` - for BOOLEAN columns
- `ps.setDate(index, value)` - for DATE columns

---

### Wildcard Patterns

Wildcards are special characters used with LIKE operator to match patterns in data.

**Main Wildcards:**

| Wildcard | Meaning | Example |
|----------|---------|---------|
| `%` | Zero or more characters | `A%` matches "A", "Aman", "Apple" |
| `_` | Exactly one character | `A_an` matches "Aman" but not "Aman" |

**In this project:**
```java
// Search for names containing "a"
ps.setString(1, "%" + "a" + "%");
// This becomes: WHERE name LIKE '%a%'

// Search for courses starting with "Java"
ps.setString(1, "Java" + "%");
// This becomes: WHERE course LIKE 'Java%'
```

**Real Examples:**
```sql
-- Find all students with "a" in their name
SELECT * FROM students WHERE name LIKE '%a%';
-- Result: Aman, Raman, Harsha, Shama

-- Find all courses starting with "Java"
SELECT * FROM students WHERE course LIKE 'Java%';
-- Result: Java, JavaWeb, JavaScript

-- Find all courses ending with "Script"
SELECT * FROM students WHERE course LIKE '%Script';
-- Result: JavaScript, TypeScript
```

---

### Auto-Increment (AUTO_INCREMENT)

AUTO_INCREMENT is a MySQL feature that automatically generates a unique number for each new row, starting from 1 and incrementing by 1.

**Syntax:**
```sql
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100),
    course VARCHAR(100),
    marks DOUBLE
);
```

**How it works:**
- When you insert a student without specifying an ID, MySQL automatically assigns the next available ID
- The ID for first student = 1
- The ID for second student = 2
- And so on...

**In this project:**
When you call `addStudent()`, you don't need to provide an ID:
```java
// Notice: no id provided
public boolean addStudent(Student student) {
    String sql = "INSERT INTO students(name,email,course,marks) VALUES(?,?,?,?)";
    // The id is automatically generated by the database
}
```

**Benefits:**
- Ensures every record has a unique identifier
- No need to manually track IDs
- Prevents duplicate IDs

---

### Data Types in Students Table

Here are the SQL data types used in your Student table:

| Data Type | Size | Description | Used For |
|-----------|------|-------------|----------|
| INT | 4 bytes | Stores integers | id (student ID) |
| VARCHAR(n) | Variable | Stores text up to n characters | name, email, course |
| DOUBLE | 8 bytes | Stores decimal numbers with precision | marks (student scores) |
| AUTO_INCREMENT | N/A | Automatically generates unique integers | id column |
| PRIMARY KEY | N/A | Uniquely identifies each row | id column |

**Example from your project:**
```sql
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,           -- Unique identifier
    name VARCHAR(100),                            -- Text up to 100 chars
    email VARCHAR(100),                           -- Text up to 100 chars
    course VARCHAR(100),                          -- Text up to 100 chars
    marks DOUBLE                                  -- Decimal number
);
```

---

### Connection Management

A Connection object represents a link between your Java application and the MySQL database. It must be opened before executing queries and closed after to free up resources.

**Pattern used in this project:**
```java
// 1. Get/Open Connection
Connection connection = DBConnection.getConnection();

// 2. Create PreparedStatement
PreparedStatement ps = connection.prepareStatement(sql);

// 3. Execute Query
ResultSet rs = ps.executeQuery();

// 4. Process Results
while (rs.next()) {
    // Extract data
}

// 5. Close Connection (Important!)
connection.close();
```

**Why close connections:**
- Prevents connection leaks
- Frees up database resources
- Allows other applications to use the database
- Improves application performance

---

### SQL Comparison with Java Operations

| Operation | SQL | Java in Project | Return Type |
|-----------|-----|-----------------|-------------|
| Add Student | INSERT | addStudent() | boolean |
| View All | SELECT * | getAllStudents() | List<Student> |
| View Single | SELECT WHERE | getStudentById() | Student |
| Update Student | UPDATE SET WHERE | updateStudent() | boolean |
| Delete Student | DELETE WHERE | deleteStudent() | boolean |
| Search by Name | SELECT LIKE | getStudentsByName() | List<Student> |
| Search by Course | SELECT LIKE | getStudentsByCourse() | List<Student> |

---

## 14. Complete SQL to Java Mapping

Here's how SQL operations map to your Java DAO methods:

### 1. Add Student (CREATE)
**SQL:**
```sql
INSERT INTO students(name, email, course, marks) VALUES ('Aman', 'aman@example.com', 'Java', 85.5);
```
**Java Method:**
```java
addStudent(new Student("Aman", "aman@example.com", "Java", 85.5))
```
**Database Change:** 1 new row added

### 2. View All Students (READ)
**SQL:**
```sql
SELECT * FROM students;
```
**Java Method:**
```java
getAllStudents()
```
**Returns:** List of all students

### 3. View Single Student (READ)
**SQL:**
```sql
SELECT * FROM students WHERE id = 1;
```
**Java Method:**
```java
getStudentById(1)
```
**Returns:** Student with id=1

### 4. Update Student (UPDATE)
**SQL:**
```sql
UPDATE students SET name=?, email=?, course=?, marks=? WHERE id=?;
```
**Java Method:**
```java
updateStudent(studentObject)
```
**Database Change:** 1 row modified

### 5. Delete Student (DELETE)
**SQL:**
```sql
DELETE FROM students WHERE id=1;
```
**Java Method:**
```java
deleteStudent(1)
```
**Database Change:** 1 row deleted

### 6. Search by Name (PATTERN MATCH)
**SQL:**
```sql
SELECT * FROM students WHERE name LIKE '%aman%';
```
**Java Method:**
```java
getStudentsByName("aman")
```
**Returns:** All students with "aman" in their name

### 7. Search by Course (PATTERN MATCH)
**SQL:**
```sql
SELECT * FROM students WHERE course LIKE '%java%';
```
**Java Method:**
```java
getStudentsByCourse("java")
```
**Returns:** All students with "java" in their course name

---

## 15. Important SQL Concepts Summary

| Concept | Purpose | Example |
|---------|---------|---------|
| **Primary Key** | Uniquely identifies each row | id INT PRIMARY KEY |
| **AUTO_INCREMENT** | Auto-generates unique values | id INT AUTO_INCREMENT |
| **WHERE Clause** | Filters rows by condition | WHERE id = 1 |
| **LIKE Operator** | Pattern matching | WHERE name LIKE '%Aman%' |
| **Wildcard %** | Matches any characters | LIKE '%text%' |
| **Wildcard _** | Matches single character | LIKE '_man' |
| **PreparedStatement** | Safely executes SQL | ps.setString(1, value) |
| **Parameter Binding** | Prevents SQL injection | Using ? placeholders |
| **ResultSet** | Holds query results | rs.next(), rs.getString() |
| **executeQuery()** | Runs SELECT | Returns ResultSet |
| **executeUpdate()** | Runs INSERT/UPDATE/DELETE | Returns int (rows affected) |

---

## 16. Common Mistakes to Avoid

1. **Not closing connections** - Always call `connection.close()` to prevent resource leaks
2. **Direct string concatenation** - Use parameter binding instead: `WHERE id = ? ` not `WHERE id = ${id}`
3. **Not checking if ResultSet has data** - Always use `if (rs.next())` before accessing data
4. **Wrong LIKE syntax** - Remember `%` means any characters, not the actual percent sign
5. **Using executeUpdate() for SELECT** - Use `executeQuery()` for SELECT queries
6. **Forgetting WHERE clause** - UPDATE/DELETE without WHERE will affect all rows!

---

## 17. Database Indexing Concept

Although not explicitly used in your current project, indexes can improve query performance.

**Without Index (Current):**
```sql
SELECT * FROM students WHERE name = 'Aman';
-- Database scans ALL rows to find match (SLOW)
```

**With Index:**
```sql
CREATE INDEX idx_name ON students(name);
SELECT * FROM students WHERE name = 'Aman';
-- Database uses index to quickly find match (FAST)
```

**When to use:**
- Frequently searched columns
- Large tables with thousands of rows
- WHERE clause conditions
- JOIN conditions

---

## 18. Prepared Statement Security

### The Problem (SQL Injection):
```java
// DANGEROUS - Never do this!
String sql = "SELECT * FROM students WHERE name = '" + name + "'";
// If name = "'; DROP TABLE students; --"
// Query becomes: SELECT * FROM students WHERE name = ''; DROP TABLE students; --'
// This would DELETE your entire table!
```

### The Solution (Parameter Binding):
```java
// SAFE - Always do this!
String sql = "SELECT * FROM students WHERE name = ?";
ps.setString(1, name);
// The database knows this is just data, not SQL code
// Even if name = "'; DROP TABLE students; --"
// It's treated as a literal string value
```

---

## 19. Future Enhancements for This Project

Based on SQL concepts, here are possible improvements:

1. **Add more search options:**
   - Search by marks range: `SELECT * FROM students WHERE marks BETWEEN 70 AND 90`
   - Sort results: `SELECT * FROM students ORDER BY marks DESC`

2. **Add filtering:**
   - Count total students: `SELECT COUNT(*) FROM students`
   - Group by course: `SELECT course, COUNT(*) FROM students GROUP BY course`

3. **Add data validation in SQL:**
   - Check marks are between 0-100
   - Check email format
   - Set default values

4. **Improve performance:**
   - Add indexes on frequently searched columns
   - Use LIMIT for pagination: `SELECT * FROM students LIMIT 10`

---

## 20. Learning Checklist

Before moving to advanced SQL, make sure you understand:

- [ ] How to write SELECT, INSERT, UPDATE, DELETE queries
- [ ] Using WHERE clause to filter data
- [ ] Using LIKE operator with wildcards
- [ ] How ResultSet works in Java
- [ ] Difference between executeQuery() and executeUpdate()
- [ ] Parameter binding with ? placeholders
- [ ] Primary keys and AUTO_INCREMENT
- [ ] Connection management (open/close)
- [ ] Why PreparedStatement is safer than raw SQL
- [ ] How to search data using LIKE patterns

Once you master these, you can learn:
- [ ] JOINs to connect multiple tables
- [ ] Aggregate functions (COUNT, SUM, AVG)
- [ ] GROUP BY and HAVING clauses
- [ ] Transactions and COMMIT/ROLLBACK
- [ ] Database relationships (One-to-Many, Many-to-Many)
