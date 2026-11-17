package com.oopproject.grades.model;

public class Course {
    private String courseId;
    private String courseName;
    private int credit;

    public Course(String courseId, String courseName, int credit) {
        setCourseId(courseId);
        setCourseName(courseName);
        setCredit(credit);
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = Person.requireText(courseId, "Course ID");
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = Person.requireText(courseName, "Course name");
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        if (credit <= 0) {
            throw new IllegalArgumentException("Credit must be greater than zero.");
        }
        this.credit = credit;
    }

    public String getDisplayName() {
        return courseId + " - " + courseName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Course)) {
            return false;
        }
        Course course = (Course) object;
        return courseId.equalsIgnoreCase(course.courseId);
    }

    @Override
    public int hashCode() {
        return courseId.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
