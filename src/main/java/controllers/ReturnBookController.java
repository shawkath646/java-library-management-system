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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Book;
import models.IssuedBook;
import models.Member;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class ReturnBookController {
    
    @FXML
    private TableView<IssuedBook> issuedBooksTable;
    
    @FXML
    private DatePicker returnDatePicker;
    
    @FXML
    private Button returnButton;
    
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
        
        issuedBooksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        issuedBooksTable.setRowFactory(tv -> new TableRow<IssuedBook>() {
            @Override
            protected void updateItem(IssuedBook item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setStyle("");
                } else if (item.isOverdue()) {
                    if (isSelected()) {
                        setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                    } else {
                        setStyle("-fx-background-color: #ffebee;");
                    }
                } else {
                    if (isSelected()) {
                        setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        
        issuedBooksTable.setOnMouseClicked(event -> {
            if (event.getTarget() == issuedBooksTable || event.getPickResult().getIntersectedNode() == null) {
                issuedBooksTable.getSelectionModel().clearSelection();
            }
        });
        
        returnDatePicker.setValue(LocalDate.now());
        
        returnButton.disableProperty().bind(
            issuedBooksTable.getSelectionModel().selectedItemProperty().isNull()
        );
        
        loadIssuedBooks();
    }
    
    private void loadIssuedBooks() {
        issuedBooksList.clear();
        var issuedBooks = issuedBookDAO.getCurrentlyIssuedBooks();
        
        for (IssuedBook issuedBook : issuedBooks) {
            Book book = bookDAO.getBookById(issuedBook.getBookId());
            if (book != null) {
                issuedBook.setBookTitle(book.getTitle());
            }
            
            Member member = memberDAO.getMemberById(issuedBook.getMemberId());
            if (member != null) {
                issuedBook.setMemberName(member.getName());
            }
        }
        
        issuedBooksList.addAll(issuedBooks);
    }
    
    @FXML
    private void handleReturnBook() {
        IssuedBook selectedBook = issuedBooksTable.getSelectionModel().getSelectedItem();
        LocalDate returnDate = returnDatePicker.getValue();
        
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
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Book Return");
        confirmDialog.setHeaderText("Are you sure you want to return this book?");
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 20; -fx-background-color: #f9f9f9; -fx-border-radius: 5;");
        
        int row = 0;
        
        Label bookHeader = new Label("Book Information");
        bookHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2196F3;");
        grid.add(bookHeader, 0, row++, 2, 1);
        
        grid.add(createLabel("Book ID:"), 0, row);
        grid.add(createValueLabel(String.valueOf(selectedBook.getBookId())), 1, row++);
        
        if (selectedBook.getBookTitle() != null) {
            grid.add(createLabel("Title:"), 0, row);
            grid.add(createValueLabel(selectedBook.getBookTitle()), 1, row++);
        }
        
        Separator sep1 = new Separator();
        sep1.setStyle("-fx-padding: 5 0 5 0;");
        grid.add(sep1, 0, row++, 2, 1);
        
        Label memberHeader = new Label("Member Information");
        memberHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2196F3;");
        grid.add(memberHeader, 0, row++, 2, 1);
        
        grid.add(createLabel("Member ID:"), 0, row);
        grid.add(createValueLabel(String.valueOf(selectedBook.getMemberId())), 1, row++);
        
        if (selectedBook.getMemberName() != null) {
            grid.add(createLabel("Name:"), 0, row);
            grid.add(createValueLabel(selectedBook.getMemberName()), 1, row++);
        }
        
        Separator sep2 = new Separator();
        sep2.setStyle("-fx-padding: 5 0 5 0;");
        grid.add(sep2, 0, row++, 2, 1);
        
        Label dateHeader = new Label("Date Information");
        dateHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2196F3;");
        grid.add(dateHeader, 0, row++, 2, 1);
        
        grid.add(createLabel("Issue Date:"), 0, row);
        grid.add(createValueLabel(selectedBook.getIssueDate().toString()), 1, row++);
        
        grid.add(createLabel("Due Date:"), 0, row);
        grid.add(createValueLabel(selectedBook.getDueDate().toString()), 1, row++);
        
        grid.add(createLabel("Return Date:"), 0, row);
        grid.add(createValueLabel(returnDate.toString()), 1, row++);
        
        long daysBorrowed = java.time.temporal.ChronoUnit.DAYS.between(selectedBook.getIssueDate(), returnDate);
        grid.add(createLabel("Days Borrowed:"), 0, row);
        grid.add(createValueLabel(daysBorrowed + " day(s)"), 1, row++);
        
        if (returnDate.isAfter(selectedBook.getDueDate())) {
            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(selectedBook.getDueDate(), returnDate);
            
            Separator sep3 = new Separator();
            sep3.setStyle("-fx-padding: 5 0 5 0;");
            grid.add(sep3, 0, row++, 2, 1);
            
            Label warningHeader = new Label("⚠ Overdue Notice");
            warningHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #f44336;");
            grid.add(warningHeader, 0, row++, 2, 1);
            
            Label warningMsg = new Label("This book is " + daysOverdue + " day(s) overdue.\nLate fees may apply.");
            warningMsg.setStyle("-fx-text-fill: #f44336; -fx-font-size: 12px;");
            warningMsg.setWrapText(true);
            grid.add(warningMsg, 0, row++, 2, 1);
        } else {
            Separator sep3 = new Separator();
            sep3.setStyle("-fx-padding: 5 0 5 0;");
            grid.add(sep3, 0, row++, 2, 1);
            
            Label onTimeLabel = new Label("✓ Book is being returned on time");
            onTimeLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            grid.add(onTimeLabel, 0, row++, 2, 1);
        }
        
        confirmDialog.getDialogPane().setContent(grid);
        confirmDialog.getDialogPane().setPrefWidth(450);
        
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
    
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #666;");
        return label;
    }
    
    private Label createValueLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #333;");
        return label;
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
