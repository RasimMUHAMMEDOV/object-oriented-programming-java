package com.oopproject.grades.model;

public abstract class Person {
    private String id;
    private String name;
    private String surname;

    protected Person(String id, String name, String surname) {
        setId(id);
        setName(name);
        setSurname(surname);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = requireText(id, "ID");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = requireText(name, "Name");
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = requireText(surname, "Surname");
    }

    protected static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }
}
