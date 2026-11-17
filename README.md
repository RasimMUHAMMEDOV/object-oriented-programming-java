# OOP
Java academic project


Student Grade Management System

Purpose:
This project is a simple Java and JavaFX application for managing students, courses, grades, averages.

**How to Run**
Requirements:
JDK 25
Maven

Run the application:
mvn javafx:run

Build the project:
mvn clean package

Features:
Adding and listing students.
Adding and listing courses.
Enter a grade for a student and course.
Show grade records.
Calculate a selected student's average.
Validate empty field duplicate IDs missing student or course selections and invalid grade values.
Load and save data automatically in CSV files.

Class Structure:
Person: abstracts parent class for common person fields.
Student: extends Person and implements Comparable<Student>. Students are sorted by name, surname, and student ID.
Course: stores course ID, course name, and credits.
Grade: connects a student ID, course ID, and grade.
GradeView: displays model used by the JavaFX grade table.
Calculatable: custom interface with calculateAverage and showResult methods.
GradeBook: main service class that manages students, courses, grades, validation, sorting, and average calculation.
CsvDataStore: handles CSV loading and saving.
MainApp: JavaFX GUI with one main screen and dialogs for adding data.

Data Files:
The program creates these files automatically in the data folder:
students.csv
courses.csv
grades.csv
