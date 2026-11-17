package com.oopproject.grades.model;

public class Grade {
    private String studentId;
    private String courseId;
    private double value;

    public Grade(String studentId, String courseId, double value) {
        setStudentId(studentId);
        setCourseId(courseId);
        setValue(value);
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = Person.requireText(studentId, "Student ID");
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = Person.requireText(courseId, "Course ID");
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100.");
        }
        this.value = value;
    }
}
