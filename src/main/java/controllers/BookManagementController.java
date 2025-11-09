package controllers;

import dao.BookDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Book;
import utils.ValidationUtil;

import java.io.IOException;
import java.util.Optional;

public class BookManagementController {
    
    @FXML
    private TextField searchField;
    
    @FXML
    private TableView<Book> booksTable;
    
    private BookDAO bookDAO;
    private ObservableList<Book> booksList;
    
    @FXML
    public void initialize() {
        bookDAO = new BookDAO();
        booksList = FXCollections.observableArrayList();
        booksTable.setItems(booksList);
        
        loadAllBooks();
    }
    
    private void loadAllBooks() {
        booksList.clear();
        booksList.addAll(bookDAO.getAllBooks());
    }
    
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadAllBooks();
        } else {
            booksList.clear();
            booksList.addAll(bookDAO.searchBooks(searchTerm));
        }
    }
    
    @FXML
    private void handleAddBook() {
        Dialog<Book> dialog = createBookDialog("Add New Book", null);
        
        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            if (bookDAO.addBook(book)) {
                showSuccess("Book added successfully!");
                loadAllBooks();
            } else {
                showError("Error adding book", "Could not add the book to the database.");
            }
        });
    }
    
    @FXML
    private void handleEditBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        
        if (selectedBook == null) {
            showWarning("No book selected", "Please select a book to edit.");
            return;
        }
        
        Dialog<Book> dialog = createBookDialog("Edit Book", selectedBook);
        
        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            book.setBookId(selectedBook.getBookId());
            if (bookDAO.updateBook(book)) {
                showSuccess("Book updated successfully!");
                loadAllBooks();
            } else {
                showError("Error updating book", "Could not update the book in the database.");
            }
        });
    }
    
    @FXML
    private void handleDeleteBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        
        if (selectedBook == null) {
            showWarning("No book selected", "Please select a book to delete.");
            return;
        }
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Book");
        confirmDialog.setContentText("Are you sure you want to delete \"" + selectedBook.getTitle() + "\"?");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (bookDAO.deleteBook(selectedBook.getBookId())) {
                showSuccess("Book deleted successfully!");
                loadAllBooks();
            } else {
                showError("Error deleting book", "Could not delete the book from the database.");
            }
        }
    }
    
    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadAllBooks();
    }
    
    @FXML
    private void handleBackToDashboard() {
        loadDashboard();
    }
    
    private Dialog<Book> createBookDialog(String title, Book book) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(book == null ? "Enter book details" : "Edit book details");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField titleField = new TextField(book != null ? book.getTitle() : "");
        TextField authorField = new TextField(book != null ? book.getAuthor() : "");
        TextField publisherField = new TextField(book != null ? book.getPublisher() : "");
        TextField categoryField = new TextField(book != null ? book.getCategory() : "");
        TextField quantityField = new TextField(book != null ? String.valueOf(book.getQuantity()) : "0");
        
        titleField.setPromptText("Title");
        authorField.setPromptText("Author");
        publisherField.setPromptText("Publisher");
        categoryField.setPromptText("Category");
        quantityField.setPromptText("Quantity");
        
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Publisher:"), 0, 2);
        grid.add(publisherField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label("Quantity:"), 0, 4);
        grid.add(quantityField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (ValidationUtil.isEmpty(titleField.getText()) || 
                    ValidationUtil.isEmpty(authorField.getText())) {
                    showWarning("Validation Error", "Title and Author are required!");
                    return null;
                }
                
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    if (!ValidationUtil.isNonNegative(quantity)) {
                        showWarning("Validation Error", "Quantity must be non-negative!");
                        return null;
                    }
                    
                    return new Book(
                        titleField.getText().trim(),
                        authorField.getText().trim(),
                        publisherField.getText().trim(),
                        categoryField.getText().trim(),
                        quantity
                    );
                } catch (NumberFormatException e) {
                    showWarning("Validation Error", "Quantity must be a valid number!");
                    return null;
                }
            }
            return null;
        });
        
        return dialog;
    }
    
    private void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/Dashboard.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) booksTable.getScene().getWindow();
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
