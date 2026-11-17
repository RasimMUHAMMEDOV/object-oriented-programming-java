package com.oopproject.grades.model;

public class GradeView {
    private final String studentId;
    private final String studentName;
    private final String courseId;
    private final String courseName;
    private final double grade;

    public GradeView(Student student, Course course, Grade grade) {
        this.studentId = student.getStudentId();
        this.studentName = student.getName() + " " + student.getSurname();
        this.courseId = course.getCourseId();
        this.courseName = course.getCourseName();
        this.grade = grade.getValue();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public double getGrade() {
        return grade;
    }
}
