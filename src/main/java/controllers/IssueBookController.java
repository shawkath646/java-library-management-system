package controllers;

import dao.BookDAO;
import dao.IssuedBookDAO;
import dao.MemberDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Book;
import models.IssuedBook;
import models.Member;

import java.io.IOException;
import java.time.LocalDate;

public class IssueBookController {
    
    @FXML
    private ComboBox<Book> bookComboBox;
    
    @FXML
    private ComboBox<Member> memberComboBox;
    
    @FXML
    private DatePicker issueDatePicker;
    
    @FXML
    private DatePicker dueDatePicker;
    
    @FXML
    private TableView<IssuedBook> issuedBooksTable;
    
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private IssuedBookDAO issuedBookDAO;
    private ObservableList<IssuedBook> issuedBooksList;
    
    @FXML
    public void initialize() {
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
        issuedBookDAO = new IssuedBookDAO();
        issuedBooksList = FXCollections.observableArrayList();
        issuedBooksTable.setItems(issuedBooksList);
        
        setupComboBoxes();
        setupDatePickers();
        loadIssuedBooks();
    }
    
    private void setupComboBoxes() {
        // Load books
        ObservableList<Book> books = FXCollections.observableArrayList(bookDAO.getAllBooks());
        bookComboBox.setItems(books);
        bookComboBox.setConverter(new StringConverter<Book>() {
            @Override
            public String toString(Book book) {
                return book != null ? book.getTitle() + " (ID: " + book.getBookId() + ")" : "";
            }
            
            @Override
            public Book fromString(String string) {
                return null;
            }
        });
        
        // Load members
        ObservableList<Member> members = FXCollections.observableArrayList(memberDAO.getAllMembers());
        memberComboBox.setItems(members);
        memberComboBox.setConverter(new StringConverter<Member>() {
            @Override
            public String toString(Member member) {
                return member != null ? member.getName() + " (ID: " + member.getMemberId() + ")" : "";
            }
            
            @Override
            public Member fromString(String string) {
                return null;
            }
        });
    }
    
    private void setupDatePickers() {
        // Set default issue date to today
        issueDatePicker.setValue(LocalDate.now());
        
        // Set default due date to 14 days from today
        dueDatePicker.setValue(LocalDate.now().plusDays(14));
    }
    
    private void loadIssuedBooks() {
        issuedBooksList.clear();
        issuedBooksList.addAll(issuedBookDAO.getCurrentlyIssuedBooks());
    }
    
    @FXML
    private void handleIssueBook() {
        Book selectedBook = bookComboBox.getValue();
        Member selectedMember = memberComboBox.getValue();
        LocalDate issueDate = issueDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();
        
        // Validation
        if (selectedBook == null) {
            showWarning("Validation Error", "Please select a book!");
            return;
        }
        
        if (selectedMember == null) {
            showWarning("Validation Error", "Please select a member!");
            return;
        }
        
        if (issueDate == null || dueDate == null) {
            showWarning("Validation Error", "Please select both issue date and due date!");
            return;
        }
        
        if (dueDate.isBefore(issueDate)) {
            showWarning("Validation Error", "Due date cannot be before issue date!");
            return;
        }
        
        // Check if book is available
        if (selectedBook.getQuantity() <= 0) {
            showWarning("Book Unavailable", "This book is currently not available!");
            return;
        }
        
        // Create issued book
        IssuedBook issuedBook = new IssuedBook(
            selectedBook.getBookId(),
            selectedMember.getMemberId(),
            issueDate,
            dueDate
        );
        
        // Issue the book
        if (issuedBookDAO.issueBook(issuedBook)) {
            showSuccess("Book issued successfully!");
            handleClear();
            loadIssuedBooks();
            
            // Refresh book list to show updated quantity
            setupComboBoxes();
        } else {
            showError("Error", "Could not issue the book. Please try again.");
        }
    }
    
    @FXML
    private void handleClear() {
        bookComboBox.setValue(null);
        memberComboBox.setValue(null);
        issueDatePicker.setValue(LocalDate.now());
        dueDatePicker.setValue(LocalDate.now().plusDays(14));
    }
    
    @FXML
    private void handleBackToDashboard() {
        loadDashboard();
    }
    
    private void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/Dashboard.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) bookComboBox.getScene().getWindow();
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
