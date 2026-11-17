package com.oopproject.grades.storage;

import com.oopproject.grades.model.Course;
import com.oopproject.grades.model.Grade;
import com.oopproject.grades.model.Student;
import com.oopproject.grades.service.GradeBook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvDataStore {
    private final File dataDirectory;
    private final File studentsFile;
    private final File coursesFile;
    private final File gradesFile;

    public CsvDataStore(String dataDirectory) {
        this.dataDirectory = new File(dataDirectory);
        this.studentsFile = new File(this.dataDirectory, "students.csv");
        this.coursesFile = new File(this.dataDirectory, "courses.csv");
        this.gradesFile = new File(this.dataDirectory, "grades.csv");
    }

    public CsvDataStore(File dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.studentsFile = new File(dataDirectory, "students.csv");
        this.coursesFile = new File(dataDirectory, "courses.csv");
        this.gradesFile = new File(dataDirectory, "grades.csv");
    }

    public void loadInto(GradeBook gradeBook) throws IOException {
        createDataFolder();

        List<Student> students = loadStudents();
        List<Course> courses = loadCourses();
        List<Grade> grades = loadGrades(students, courses);
        gradeBook.replaceAll(students, courses, grades);
    }

    public void save(GradeBook gradeBook) throws IOException {
        createDataFolder();
        saveStudents(gradeBook.getStudents());
        saveCourses(gradeBook.getCourses());
        saveGrades(gradeBook.getGrades());
    }

    private List<Student> loadStudents() throws IOException {
        List<Student> students = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(studentsFile));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            if (parts.length == 4) {
                students.add(new Student(parts[0], parts[1], parts[2], parts[3]));
            }
            line = reader.readLine();
        }
        reader.close();
        return students;
    }

    private List<Course> loadCourses() throws IOException {
        List<Course> courses = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(coursesFile));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                courses.add(new Course(parts[0], parts[1], Integer.parseInt(parts[2])));
            }
            line = reader.readLine();
        }
        reader.close();
        return courses;
    }

    private List<Grade> loadGrades(List<Student> students, List<Course> courses) throws IOException {
        List<Grade> grades = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(gradesFile));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            if (parts.length == 3 && studentExists(students, parts[0]) && courseExists(courses, parts[1])) {
                grades.add(new Grade(parts[0], parts[1], Double.parseDouble(parts[2])));
            }
            line = reader.readLine();
        }
        reader.close();
        return grades;
    }

    private void saveStudents(List<Student> students) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(studentsFile));
        for (Student student : students) {
            writer.write(student.getStudentId() + "," + student.getName() + "," + student.getSurname() + ","
                    + student.getDepartment());
            writer.newLine();
        }
        writer.close();
    }

    private void saveCourses(List<Course> courses) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(coursesFile));
        for (Course course : courses) {
            writer.write(course.getCourseId() + "," + course.getCourseName() + "," + course.getCredit());
            writer.newLine();
        }
        writer.close();
    }

    private void saveGrades(List<Grade> grades) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(gradesFile));
        for (Grade grade : grades) {
            writer.write(grade.getStudentId() + "," + grade.getCourseId() + "," + grade.getValue());
            writer.newLine();
        }
        writer.close();
    }

    private boolean studentExists(List<Student> students, String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(studentId)) {
                return true;
            }
        }
        return false;
    }

    private boolean courseExists(List<Course> courses, String courseId) {
        for (Course course : courses) {
            if (course.getCourseId().equalsIgnoreCase(courseId)) {
                return true;
            }
        }
        return false;
    }

    private void createDataFolder() throws IOException {
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
        if (!studentsFile.exists()) {
            studentsFile.createNewFile();
        }
        if (!coursesFile.exists()) {
            coursesFile.createNewFile();
        }
        if (!gradesFile.exists()) {
            gradesFile.createNewFile();
        }
    }
}
