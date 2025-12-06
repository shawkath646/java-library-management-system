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
    private ComboBox<String> filterComboBox;
    
    @FXML
    private DatePicker issueDatePicker;
    
    @FXML
    private DatePicker dueDatePicker;
    
    @FXML
    private TableView<IssuedBook> issuedBooksTable;
    
    @FXML
    private Label bookInfoLabel;
    
    @FXML
    private Label memberInfoLabel;
    
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private IssuedBookDAO issuedBookDAO;
    private ObservableList<IssuedBook> issuedBooksList;
    private ObservableList<IssuedBook> allIssuedBooks;
    
    @FXML
    public void initialize() {
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
        issuedBookDAO = new IssuedBookDAO();
        issuedBooksList = FXCollections.observableArrayList();
        allIssuedBooks = FXCollections.observableArrayList();
        issuedBooksTable.setItems(issuedBooksList);
        
        issuedBooksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        issuedBooksTable.setOnMouseClicked(event -> {
            if (event.getTarget() == issuedBooksTable || event.getPickResult().getIntersectedNode() == null) {
                issuedBooksTable.getSelectionModel().clearSelection();
            }
        });
        
        setupComboBoxes();
        setupDatePickers();
        setupFilterComboBox();
        loadIssuedBooks();
    }
    
    private void setupFilterComboBox() {
        filterComboBox.setValue("All");
        filterComboBox.setOnAction(event -> applyDateFilter());
    }
    
    private void applyDateFilter() {
        String filter = filterComboBox.getValue();
        if (filter == null || filter.equals("All")) {
            issuedBooksList.clear();
            issuedBooksList.addAll(allIssuedBooks);
            return;
        }
        
        LocalDate today = LocalDate.now();
        LocalDate startDate = null;
        
        switch (filter) {
            case "Today":
                startDate = today;
                break;
            case "Yesterday":
                startDate = today.minusDays(1);
                break;
            case "This Week":
                startDate = today.minusDays(today.getDayOfWeek().getValue() - 1);
                break;
            case "This Month":
                startDate = today.withDayOfMonth(1);
                break;
        }
        
        if (startDate != null) {
            LocalDate finalStartDate = startDate;
            LocalDate endDate = filter.equals("Yesterday") ? startDate : today;
            
            issuedBooksList.clear();
            issuedBooksList.addAll(
                allIssuedBooks.stream()
                    .filter(book -> {
                        LocalDate issueDate = book.getIssueDate();
                        return !issueDate.isBefore(finalStartDate) && !issueDate.isAfter(endDate);
                    })
                    .toList()
            );
        }
    }
    
    private void setupComboBoxes() {
        ObservableList<Book> books = FXCollections.observableArrayList(bookDAO.getAllBooks());
        bookComboBox.setItems(books);
        bookComboBox.setConverter(new StringConverter<Book>() {
            @Override
            public String toString(Book book) {
                if (book != null) {
                    return String.format("%s - %s (Qty: %d)", book.getTitle(), book.getAuthor(), book.getQuantity());
                }
                return "";
            }
            
            @Override
            public Book fromString(String string) {
                return null;
            }
        });
        
        bookComboBox.setOnAction(event -> {
            Book selectedBook = bookComboBox.getValue();
            if (selectedBook != null) {
                String info = String.format("Available: %d copies | Category: %s | Publisher: %s",
                    selectedBook.getQuantity(), selectedBook.getCategory(), selectedBook.getPublisher());
                bookInfoLabel.setText(info);
            } else {
                bookInfoLabel.setText("");
            }
        });
        
        ObservableList<Member> members = FXCollections.observableArrayList(memberDAO.getAllMembers());
        memberComboBox.setItems(members);
        memberComboBox.setConverter(new StringConverter<Member>() {
            @Override
            public String toString(Member member) {
                if (member != null) {
                    return String.format("%s - %s", member.getName(), member.getEmail());
                }
                return "";
            }
            
            @Override
            public Member fromString(String string) {
                return null;
            }
        });
        
        memberComboBox.setOnAction(event -> {
            Member selectedMember = memberComboBox.getValue();
            if (selectedMember != null) {
                showMemberBorrowedBooks(selectedMember.getMemberId());
                int borrowedCount = (int) issuedBooksList.stream()
                    .filter(book -> !book.isReturned())
                    .count();
                String info = String.format("Currently borrowed: %d books | Phone: %s",
                    borrowedCount, selectedMember.getPhone());
                memberInfoLabel.setText(info);
            } else {
                memberInfoLabel.setText("");
                loadIssuedBooks();
            }
        });
    }
    
    private void showMemberBorrowedBooks(int memberId) {
        issuedBooksList.clear();
        
        var memberBooks = allIssuedBooks.stream()
            .filter(book -> book.getMemberId() == memberId)
            .toList();
        
        for (IssuedBook issuedBook : memberBooks) {
            Book book = bookDAO.getBookById(issuedBook.getBookId());
            if (book != null) {
                issuedBook.setBookTitle(book.getTitle());
            }
        }
        
        issuedBooksList.addAll(memberBooks);
    }
    
    private void setupDatePickers() {
        issueDatePicker.setValue(LocalDate.now());
        
        dueDatePicker.setValue(LocalDate.now().plusDays(14));
    }
    
    private void loadIssuedBooks() {
        allIssuedBooks.clear();
        issuedBooksList.clear();
        var issuedBooks = issuedBookDAO.getCurrentlyIssuedBooks();
        
        for (IssuedBook issuedBook : issuedBooks) {
            Book book = bookDAO.getBookById(issuedBook.getBookId());
            if (book != null) {
                issuedBook.setBookTitle(book.getTitle());
            }
        }
        
        allIssuedBooks.addAll(issuedBooks);
        issuedBooksList.addAll(issuedBooks);
    }
    
    @FXML
    private void handleIssueBook() {
        Book selectedBook = bookComboBox.getValue();
        Member selectedMember = memberComboBox.getValue();
        LocalDate issueDate = issueDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();
        
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
        
        if (selectedBook.getQuantity() <= 0) {
            showWarning("Book Unavailable", "This book is currently not available!");
            return;
        }
        
        IssuedBook issuedBook = new IssuedBook(
            selectedBook.getBookId(),
            selectedMember.getMemberId(),
            issueDate,
            dueDate
        );
        
        if (issuedBookDAO.issueBook(issuedBook)) {
            showSuccess("Book issued successfully!");
            handleClear();
            loadIssuedBooks();
            
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
            Scene scene = stage.getScene();
            
            scene.setRoot(root);
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
