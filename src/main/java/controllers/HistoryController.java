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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Book;
import models.IssuedBook;
import models.Member;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class HistoryController {
    
    @FXML
    private TableView<IssuedBook> historyTable;
    
    @FXML
    private TableColumn<IssuedBook, Integer> issueIdColumn;
    
    @FXML
    private TableColumn<IssuedBook, String> bookTitleColumn;
    
    @FXML
    private TableColumn<IssuedBook, String> memberNameColumn;
    
    @FXML
    private TableColumn<IssuedBook, LocalDate> issueDateColumn;
    
    @FXML
    private TableColumn<IssuedBook, LocalDate> dueDateColumn;
    
    @FXML
    private TableColumn<IssuedBook, LocalDate> returnDateColumn;
    
    @FXML
    private TableColumn<IssuedBook, String> statusColumn;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Label totalReturnedLabel;
    
    @FXML
    private Label onTimeReturnsLabel;
    
    @FXML
    private Label lateReturnsLabel;
    
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private IssuedBookDAO issuedBookDAO;
    private ObservableList<IssuedBook> historyList;
    private ObservableList<IssuedBook> allHistory;
    
    @FXML
    public void initialize() {
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
        issuedBookDAO = new IssuedBookDAO();
        historyList = FXCollections.observableArrayList();
        allHistory = FXCollections.observableArrayList();
        
        setupTableColumns();
        loadHistory();
        updateStatistics();
        setupSearch();
        
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        historyTable.setRowFactory(tv -> new TableRow<IssuedBook>() {
            @Override
            protected void updateItem(IssuedBook item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null || item.getReturnDate() == null) {
                    setStyle("");
                } else if (item.getReturnDate().isAfter(item.getDueDate())) {
                    // Late return - red background
                    if (isSelected()) {
                        setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                    } else {
                        setStyle("-fx-background-color: #ffebee;");
                    }
                } else {
                    // On-time return - green background
                    if (isSelected()) {
                        setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                    } else {
                        setStyle("-fx-background-color: #e8f5e9;");
                    }
                }
            }
        });
    }
    
    private void setupTableColumns() {
        issueIdColumn.setCellValueFactory(new PropertyValueFactory<>("issueId"));
        
        bookTitleColumn.setCellValueFactory(cellData -> {
            int bookId = cellData.getValue().getBookId();
            Book book = bookDAO.getBookById(bookId);
            return new javafx.beans.property.SimpleStringProperty(
                book != null ? book.getTitle() : "Unknown"
            );
        });
        
        memberNameColumn.setCellValueFactory(cellData -> {
            int memberId = cellData.getValue().getMemberId();
            Member member = memberDAO.getMemberById(memberId);
            return new javafx.beans.property.SimpleStringProperty(
                member != null ? member.getName() : "Unknown"
            );
        });
        
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        
        statusColumn.setCellValueFactory(cellData -> {
            IssuedBook issuedBook = cellData.getValue();
            if (issuedBook.getReturnDate() == null) {
                return new javafx.beans.property.SimpleStringProperty("Not Returned");
            } else if (issuedBook.getReturnDate().isAfter(issuedBook.getDueDate())) {
                long daysLate = java.time.temporal.ChronoUnit.DAYS.between(
                    issuedBook.getDueDate(), 
                    issuedBook.getReturnDate()
                );
                return new javafx.beans.property.SimpleStringProperty("Late (" + daysLate + " days)");
            } else {
                return new javafx.beans.property.SimpleStringProperty("On Time");
            }
        });
        
        statusColumn.setCellFactory(column -> new TableCell<IssuedBook, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.startsWith("Late")) {
                        setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
                    } else if (item.equals("On Time")) {
                        setStyle("-fx-text-fill: #388e3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }
    
    private void loadHistory() {
        List<IssuedBook> returnedBooks = issuedBookDAO.getReturnedBooks();
        allHistory.clear();
        allHistory.addAll(returnedBooks);
        historyList.clear();
        historyList.addAll(returnedBooks);
        historyTable.setItems(historyList);
    }
    
    private void updateStatistics() {
        int totalReturned = allHistory.size();
        int onTimeReturns = 0;
        int lateReturns = 0;
        
        for (IssuedBook book : allHistory) {
            if (book.getReturnDate() != null) {
                if (book.getReturnDate().isAfter(book.getDueDate())) {
                    lateReturns++;
                } else {
                    onTimeReturns++;
                }
            }
        }
        
        totalReturnedLabel.setText(String.valueOf(totalReturned));
        onTimeReturnsLabel.setText(String.valueOf(onTimeReturns));
        lateReturnsLabel.setText(String.valueOf(lateReturns));
    }
    
    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterHistory(newValue);
        });
    }
    
    private void filterHistory(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            historyList.clear();
            historyList.addAll(allHistory);
            return;
        }
        
        String lowerCaseFilter = searchText.toLowerCase().trim();
        ObservableList<IssuedBook> filteredList = FXCollections.observableArrayList();
        
        for (IssuedBook issuedBook : allHistory) {
            Book book = bookDAO.getBookById(issuedBook.getBookId());
            Member member = memberDAO.getMemberById(issuedBook.getMemberId());
            
            boolean matches = false;
            
            if (book != null && book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                matches = true;
            }
            if (member != null && member.getName().toLowerCase().contains(lowerCaseFilter)) {
                matches = true;
            }
            if (String.valueOf(issuedBook.getIssueId()).contains(lowerCaseFilter)) {
                matches = true;
            }
            
            if (matches) {
                filteredList.add(issuedBook);
            }
        }
        
        historyList.clear();
        historyList.addAll(filteredList);
    }
    
    @FXML
    private void handleRefresh() {
        loadHistory();
        updateStatistics();
        searchField.clear();
        showAlert(Alert.AlertType.INFORMATION, "Success", "History refreshed successfully!");
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/Dashboard.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) historyTable.getScene().getWindow();
            Scene scene = stage.getScene();
            
            scene.setRoot(root);
            stage.setTitle("Library Management System - Dashboard");
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load dashboard: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
