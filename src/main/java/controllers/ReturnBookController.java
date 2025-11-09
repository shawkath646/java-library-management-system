package controllers;

import dao.IssuedBookDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.IssuedBook;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class ReturnBookController {
    
    @FXML
    private TableView<IssuedBook> issuedBooksTable;
    
    @FXML
    private DatePicker returnDatePicker;
    
    private IssuedBookDAO issuedBookDAO;
    private ObservableList<IssuedBook> issuedBooksList;
    
    @FXML
    public void initialize() {
        issuedBookDAO = new IssuedBookDAO();
        issuedBooksList = FXCollections.observableArrayList();
        issuedBooksTable.setItems(issuedBooksList);
        
        // Set default return date to today
        returnDatePicker.setValue(LocalDate.now());
        
        loadIssuedBooks();
    }
    
    private void loadIssuedBooks() {
        issuedBooksList.clear();
        issuedBooksList.addAll(issuedBookDAO.getCurrentlyIssuedBooks());
    }
    
    @FXML
    private void handleReturnBook() {
        IssuedBook selectedBook = issuedBooksTable.getSelectionModel().getSelectedItem();
        LocalDate returnDate = returnDatePicker.getValue();
        
        // Validation
        if (selectedBook == null) {
            showWarning("No book selected", "Please select a book to return.");
            return;
        }
        
        if (returnDate == null) {
            showWarning("Validation Error", "Please select a return date!");
            return;
        }
        
        if (returnDate.isBefore(selectedBook.getIssueDate())) {
            showWarning("Validation Error", "Return date cannot be before issue date!");
            return;
        }
        
        // Confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Return");
        confirmDialog.setHeaderText("Return Book");
        
        String overdueMessage = "";
        if (returnDate.isAfter(selectedBook.getDueDate())) {
            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(selectedBook.getDueDate(), returnDate);
            overdueMessage = "\n\nWARNING: This book is " + daysOverdue + " day(s) overdue!";
        }
        
        confirmDialog.setContentText(
            "Book ID: " + selectedBook.getBookId() + 
            "\nMember ID: " + selectedBook.getMemberId() + 
            "\nIssue Date: " + selectedBook.getIssueDate() +
            "\nDue Date: " + selectedBook.getDueDate() +
            "\nReturn Date: " + returnDate +
            overdueMessage +
            "\n\nConfirm return?"
        );
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedBook.setReturnDate(returnDate);
            
            if (issuedBookDAO.returnBook(selectedBook.getIssueId(), returnDate)) {
                showSuccess("Book returned successfully!");
                loadIssuedBooks();
                returnDatePicker.setValue(LocalDate.now());
            } else {
                showError("Error", "Could not return the book. Please try again.");
            }
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadIssuedBooks();
        returnDatePicker.setValue(LocalDate.now());
    }
    
    @FXML
    private void handleBackToDashboard() {
        loadDashboard();
    }
    
    private void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/Dashboard.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) issuedBooksTable.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/style.css").toExternalForm());
            
            stage.setScene(scene);
            stage.setTitle("Library Management System - Dashboard");
            
        } catch (IOException e) {
            showError("Error", "Could not load dashboard: " + e.getMessage());
        }
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
