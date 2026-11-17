package com.oopproject.grades.service;

public interface Calculatable {
    double calculateAverage(String studentId);

    String showResult(String studentId);
}
