package com.oopproject.grades;

import com.oopproject.grades.model.Course;
import com.oopproject.grades.model.GradeView;
import com.oopproject.grades.model.Student;
import com.oopproject.grades.service.GradeBook;
import com.oopproject.grades.storage.CsvDataStore;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MainApp extends Application {
    private final GradeBook gradeBook = new GradeBook();
    private final CsvDataStore dataStore = new CsvDataStore("data");
    private final ObservableList<Student> studentRows = FXCollections.observableArrayList();
    private final ObservableList<Course> courseRows = FXCollections.observableArrayList();
    private final ObservableList<GradeView> gradeRows = FXCollections.observableArrayList();

    private TableView<Student> studentTable;
    private TableView<Course> courseTable;
    private TableView<GradeView> gradeTable;

    @Override
    public void start(Stage stage) {
        loadData();

        BorderPane root = new BorderPane();
        root.setTop(createToolbar());
        root.setCenter(createTables());

        refreshTables();

        Scene scene = new Scene(root, 1050, 680);
        stage.setTitle("Student Grade Management System");
        stage.setScene(scene);
        stage.show();
    }

    private HBox createToolbar() {
        Button addStudentButton = new Button("Add Student");
        addStudentButton.setOnAction(event -> showStudentDialog());

        Button addCourseButton = new Button("Add Course");
        addCourseButton.setOnAction(event -> showCourseDialog());

        Button addGradeButton = new Button("Add Grade");
        addGradeButton.setOnAction(event -> showGradeDialog());

        Button averageButton = new Button("Calculate Average");
        averageButton.setOnAction(event -> showAverageDialog());

        Button showGradesButton = new Button("Show Grades");
        showGradesButton.setOnAction(event -> {
            refreshTables();
            showInfo("Grade information is shown in the Grades table.");
        });

        HBox toolbar = new HBox(10, addStudentButton, addCourseButton, addGradeButton, showGradesButton, averageButton);
        toolbar.setPadding(new Insets(14));
        return toolbar;
    }

    private HBox createTables() {
        studentTable = createStudentTable();
        courseTable = createCourseTable();
        gradeTable = createGradeTable();

        VBox studentsBox = section("Students", studentTable);
        VBox coursesBox = section("Courses", courseTable);
        VBox gradesBox = section("Grades", gradeTable);

        HBox tables = new HBox(14, studentsBox, coursesBox, gradesBox);
        tables.setPadding(new Insets(0, 14, 14, 14));
        HBox.setHgrow(studentsBox, Priority.ALWAYS);
        HBox.setHgrow(coursesBox, Priority.ALWAYS);
        HBox.setHgrow(gradesBox, Priority.ALWAYS);
        return tables;
    }

    private VBox section(String title, TableView<?> table) {
        Label label = new Label(title);
        VBox box = new VBox(8, label, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    private TableView<Student> createStudentTable() {
        TableView<Student> table = new TableView<>(studentRows);

        TableColumn<Student, String> id = new TableColumn<>("Student ID");
        id.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentId()));
        id.setPrefWidth(100);

        TableColumn<Student, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        name.setPrefWidth(100);

        TableColumn<Student, String> surname = new TableColumn<>("Surname");
        surname.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSurname()));
        surname.setPrefWidth(100);

        TableColumn<Student, String> department = new TableColumn<>("Department");
        department.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDepartment()));
        department.setPrefWidth(120);

        table.getColumns().add(id);
        table.getColumns().add(name);
        table.getColumns().add(surname);
        table.getColumns().add(department);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        return table;
    }

    private TableView<Course> createCourseTable() {
        TableView<Course> table = new TableView<>(courseRows);

        TableColumn<Course, String> id = new TableColumn<>("Course ID");
        id.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourseId()));
        id.setPrefWidth(100);

        TableColumn<Course, String> name = new TableColumn<>("Course Name");
        name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourseName()));
        name.setPrefWidth(160);

        TableColumn<Course, Number> credit = new TableColumn<>("Credit");
        credit.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCredit()));
        credit.setPrefWidth(70);

        table.getColumns().add(id);
        table.getColumns().add(name);
        table.getColumns().add(credit);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        return table;
    }

    private TableView<GradeView> createGradeTable() {
        TableView<GradeView> table = new TableView<>(gradeRows);

        TableColumn<GradeView, String> student = new TableColumn<>("Student");
        student.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentName()));
        student.setPrefWidth(140);

        TableColumn<GradeView, String> course = new TableColumn<>("Course");
        course.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourseName()));
        course.setPrefWidth(140);

        TableColumn<GradeView, Number> grade = new TableColumn<>("Grade");
        grade.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getGrade()));
        grade.setPrefWidth(70);

        table.getColumns().add(student);
        table.getColumns().add(course);
        table.getColumns().add(grade);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        return table;
    }

    private void showStudentDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add Student");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField departmentField = new TextField();
        dialog.getDialogPane().setContent(form(
                row("Student ID", idField),
                row("Name", nameField),
                row("Surname", surnameField),
                row("Department", departmentField)));
        dialog.setResultConverter(button -> button == ButtonType.OK
                ? new Student(idField.getText(), nameField.getText(), surnameField.getText(), departmentField.getText())
                : null);

        try {
            Optional<Student> result = dialog.showAndWait();
            if (result.isPresent()) {
                gradeBook.addStudent(result.get());
                saveData();
                refreshTables();
                showInfo("Saved successfully.");
            }
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void showCourseDialog() {
        Dialog<Course> dialog = new Dialog<>();
        dialog.setTitle("Add Course");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField creditField = new TextField();
        dialog.getDialogPane().setContent(form(
                row("Course ID", idField),
                row("Course Name", nameField),
                row("Credit", creditField)));
        dialog.setResultConverter(button -> button == ButtonType.OK
                ? new Course(idField.getText(), nameField.getText(), parseInteger(creditField.getText(), "Credit"))
                : null);

        try {
            Optional<Course> result = dialog.showAndWait();
            if (result.isPresent()) {
                gradeBook.addCourse(result.get());
                saveData();
                refreshTables();
                showInfo("Saved successfully.");
            }
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void showGradeDialog() {
        if (gradeBook.getStudents().isEmpty() || gradeBook.getCourses().isEmpty()) {
            showError("Please add at least one student and one course before entering grades.");
            return;
        }

        Dialog<GradeEntry> dialog = new Dialog<>();
        dialog.setTitle("Add Grade");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Student> studentBox = new ComboBox<>(FXCollections.observableArrayList(gradeBook.getStudents()));
        ComboBox<Course> courseBox = new ComboBox<>(FXCollections.observableArrayList(gradeBook.getCourses()));
        TextField gradeField = new TextField();
        studentBox.setMaxWidth(Double.MAX_VALUE);
        courseBox.setMaxWidth(Double.MAX_VALUE);

        dialog.getDialogPane().setContent(form(
                row("Student", studentBox),
                row("Course", courseBox),
                row("Grade", gradeField)));

        dialog.setResultConverter(button -> {
            if (button != ButtonType.OK) {
                return null;
            }
            validateGradeInput(studentBox, courseBox, gradeField);
            return new GradeEntry(
                    studentBox.getValue().getStudentId(),
                    courseBox.getValue().getCourseId(),
                    parseDouble(gradeField.getText(), "Grade"));
        });

        try {
            Optional<GradeEntry> result = dialog.showAndWait();
            if (result.isPresent()) {
                GradeEntry entry = result.get();
                gradeBook.addGrade(entry.getStudentId(), entry.getCourseId(), entry.getValue());
                saveData();
                refreshTables();
                showInfo("Saved successfully.");
            }
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void showAverageDialog() {
        if (gradeBook.getStudents().isEmpty()) {
            showError("Please add a student first.");
            return;
        }

        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Calculate Average");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Student> studentBox = new ComboBox<>(FXCollections.observableArrayList(gradeBook.getStudents()));
        studentBox.setMaxWidth(Double.MAX_VALUE);
        dialog.getDialogPane().setContent(form(row("Student", studentBox)));

        dialog.setResultConverter(button -> {
            if (button != ButtonType.OK) {
                return null;
            }
            if (studentBox.getValue() == null) {
                throw new IllegalArgumentException("Please select a student.");
            }
            return studentBox.getValue();
        });

        try {
            Optional<Student> result = dialog.showAndWait();
            result.ifPresent(student -> showInfo(gradeBook.showResult(student.getStudentId())));
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void validateGradeInput(ComboBox<Student> studentBox, ComboBox<Course> courseBox, TextField gradeField) {
        if (studentBox.getValue() == null) {
            throw new IllegalArgumentException("Please select a student.");
        }
        if (courseBox.getValue() == null) {
            throw new IllegalArgumentException("Please select a course.");
        }
        double grade = parseDouble(gradeField.getText(), "Grade");
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100.");
        }
    }

    private GridPane form(FormRow... rows) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(12));

        for (int i = 0; i < rows.length; i++) {
            Label label = new Label(rows[i].label());
            grid.add(label, 0, i);
            grid.add(rows[i].control(), 1, i);
            GridPane.setHgrow(rows[i].control(), Priority.ALWAYS);
        }
        return grid;
    }

    private FormRow row(String label, javafx.scene.Node control) {
        return new FormRow(label, control);
    }

    private int parseInteger(String text, String fieldName) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " must be a whole number.");
        }
    }

    private double parseDouble(String text, String fieldName) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " must be a number.");
        }
    }

    private void loadData() {
        try {
            dataStore.loadInto(gradeBook);
        } catch (IOException | RuntimeException exception) {
            showError("Data could not be loaded: " + exception.getMessage());
        }
    }

    private void saveData() {
        try {
            dataStore.save(gradeBook);
        } catch (IOException exception) {
            showError("Data could not be saved: " + exception.getMessage());
        }
    }

    private void refreshTables() {
        studentRows.setAll(gradeBook.getStudents());
        courseRows.setAll(gradeBook.getCourses());
        gradeRows.setAll(gradeBook.getGradeViews());
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class FormRow {
        private String label;
        private javafx.scene.Node control;

        public FormRow(String label, javafx.scene.Node control) {
            this.label = label;
            this.control = control;
        }

        public String label() {
            return label;
        }

        public javafx.scene.Node control() {
            return control;
        }
    }

    private static class GradeEntry {
        private String studentId;
        private String courseId;
        private double value;

        public GradeEntry(String studentId, String courseId, double value) {
            this.studentId = studentId;
            this.courseId = courseId;
            this.value = value;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getCourseId() {
            return courseId;
        }

        public double getValue() {
            return value;
        }
    }
}
