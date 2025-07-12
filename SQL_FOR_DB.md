# 📚 Library Management System - SQL Schema

A lightweight SQL-based design for managing books, students, and admins in a library environment

---

## 📖 Overview

This repository contains the **SQL schema** for a basic but complete **Library Management System**. It includes tables for managing:

- 📘 Book records and stock
- 🎓 Students and their course details
- 👨‍💼 Admin login and authentication
- 📗 Issuing and returning books

---

## 🧩 SQL Schema

```sql
-- Book Details
CREATE TABLE book_details (
    BookID VARCHAR(50) PRIMARY KEY,
    Book_Name VARCHAR(255) NOT NULL,
    Author VARCHAR(255) NOT NULL
);

-- Book Stock
CREATE TABLE book_stock (
    BookID VARCHAR(50),
    Quantity INT NOT NULL DEFAULT 0,
    FOREIGN KEY (BookID) REFERENCES book_details(BookID)
);

-- Student Table
CREATE TABLE student (
    StudentID VARCHAR(50) PRIMARY KEY,
    Student_Name VARCHAR(255) NOT NULL,
    Course ENUM('IT', 'ENG', 'ACC', 'MNG', 'THM', 'EN') NOT NULL,
    Contact_Number VARCHAR(20),
    Photo BLOB,
    Address TEXT
);

-- Admin Table
CREATE TABLE admin (
    ID_Number VARCHAR(50) PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Contact VARCHAR(20)
);

-- Login Table
CREATE TABLE login (
    ID_Number VARCHAR(50),
    email VARCHAR(255),
    password VARCHAR(255),
    FOREIGN KEY (ID_Number) REFERENCES admin(ID_Number)
);

-- Signin Table
CREATE TABLE signin (
    LoginID VARCHAR(255),
    ID_Number VARCHAR(50),
    Verify_Code INT,
    FOREIGN KEY (ID_Number) REFERENCES admin(ID_Number)
);

-- Issue Book Details
CREATE TABLE issue_book_details (
    BookID VARCHAR(50),
    StudentID VARCHAR(50),
    Issue_Date DATE NOT NULL,
    Due_Date DATE NOT NULL,
    Status ENUM('pending', 'returned') DEFAULT 'pending',
    FOREIGN KEY (BookID) REFERENCES book_details(BookID),
    FOREIGN KEY (StudentID) REFERENCES student(StudentID)
);

-- Sample Data
INSERT INTO admin (ID_Number, Name, Contact) 
VALUES ('ADMIN001', 'Admin', 'XXXXXXXXXXX');

INSERT INTO login (ID_Number, email, password) 
VALUES ('ADMIN001', 'admin@example.com', 'password');
