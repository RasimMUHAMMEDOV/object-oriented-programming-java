package com.oopproject.grades.service;

import com.oopproject.grades.model.Course;
import com.oopproject.grades.model.Grade;
import com.oopproject.grades.model.GradeView;
import com.oopproject.grades.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GradeBook implements Calculatable {
    private final List<Student> students = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<Grade> grades = new ArrayList<>();

    public void addStudent(Student student) {
        if (findStudent(student.getStudentId()) != null) {
            throw new IllegalArgumentException("A student with this ID already exists.");
        }
        students.add(student);
        Collections.sort(students);
    }

    public void addCourse(Course course) {
        if (findCourse(course.getCourseId()) != null) {
            throw new IllegalArgumentException("A course with this ID already exists.");
        }
        courses.add(course);
        sortCourses();
    }

    public void addGrade(String studentId, String courseId, double value) {
        Student student = findStudent(studentId);
        Course course = findCourse(courseId);

        if (student == null) {
            throw new IllegalArgumentException("Student was not found.");
        }
        if (course == null) {
            throw new IllegalArgumentException("Course was not found.");
        }

        Grade grade = new Grade(student.getStudentId(), course.getCourseId(), value);
        for (int i = 0; i < grades.size(); i++) {
            Grade existing = grades.get(i);
            if (existing.getStudentId().equalsIgnoreCase(studentId)
                    && existing.getCourseId().equalsIgnoreCase(courseId)) {
                grades.remove(i);
                break;
            }
        }
        grades.add(grade);
    }

    @Override
    public double calculateAverage(String studentId) {
        if (findStudent(studentId) == null) {
            throw new IllegalArgumentException("Student was not found.");
        }

        double total = 0;
        int count = 0;
        for (Grade grade : grades) {
            if (grade.getStudentId().equalsIgnoreCase(studentId)) {
                total = total + grade.getValue();
                count++;
            }
        }

        if (count == 0) {
            throw new IllegalArgumentException("This student has no grades yet.");
        }

        return total / count;
    }

    @Override
    public String showResult(String studentId) {
        Student student = findStudent(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student was not found.");
        }
        return student.getDisplayName() + " average grade: " + String.format("%.2f", calculateAverage(studentId));
    }

    public Student findStudent(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(studentId.trim())) {
                return student;
            }
        }
        return null;
    }

    public Course findCourse(String courseId) {
        for (Course course : courses) {
            if (course.getCourseId().equalsIgnoreCase(courseId.trim())) {
                return course;
            }
        }
        return null;
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public List<Grade> getGrades() {
        return Collections.unmodifiableList(grades);
    }

    public List<GradeView> getGradeViews() {
        List<GradeView> gradeViews = new ArrayList<>();
        for (Grade grade : grades) {
            Student student = findStudent(grade.getStudentId());
            Course course = findCourse(grade.getCourseId());
            if (student != null && course != null) {
                gradeViews.add(new GradeView(student, course, grade));
            }
        }
        return gradeViews;
    }

    public void replaceAll(List<Student> loadedStudents, List<Course> loadedCourses, List<Grade> loadedGrades) {
        students.clear();
        courses.clear();
        grades.clear();
        students.addAll(loadedStudents);
        courses.addAll(loadedCourses);
        grades.addAll(loadedGrades);
        Collections.sort(students);
        sortCourses();
    }

    private void sortCourses() {
        for (int i = 0; i < courses.size() - 1; i++) {
            for (int j = i + 1; j < courses.size(); j++) {
                Course first = courses.get(i);
                Course second = courses.get(j);
                if (first.getCourseId().compareToIgnoreCase(second.getCourseId()) > 0) {
                    courses.set(i, second);
                    courses.set(j, first);
                }
            }
        }
    }
}
