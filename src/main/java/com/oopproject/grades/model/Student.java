package com.oopproject.grades.model;

public class Student extends Person implements Comparable<Student> {
    private String department;

    public Student(String studentId, String name, String surname, String department) {
        super(studentId, name, surname);
        setDepartment(department);
    }

    public String getStudentId() {
        return getId();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = requireText(department, "Department");
    }

    public String getDisplayName() {
        return getName() + " " + getSurname() + " (" + getStudentId() + ")";
    }

    @Override
    public int compareTo(Student other) {
        int byName = getName().compareToIgnoreCase(other.getName());
        if (byName != 0) {
            return byName;
        }

        int bySurname = getSurname().compareToIgnoreCase(other.getSurname());
        if (bySurname != 0) {
            return bySurname;
        }

        return getStudentId().compareToIgnoreCase(other.getStudentId());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Student)) {
            return false;
        }
        Student student = (Student) object;
        return getStudentId().equalsIgnoreCase(student.getStudentId());
    }

    @Override
    public int hashCode() {
        return getStudentId().toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
