package controllers;

import dao.BookDAO;
import dao.IssuedBookDAO;
import dao.MemberDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    
    @FXML
    private Label totalBooksLabel;
    
    @FXML
    private Label totalMembersLabel;
    
    @FXML
    private Label currentlyIssuedLabel;
    
    @FXML
    private Label overdueLabel;
    
    @FXML
    private Label statusLabel;
    
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private IssuedBookDAO issuedBookDAO;
    
    @FXML
    public void initialize() {
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
        issuedBookDAO = new IssuedBookDAO();
        
        loadStatistics();
        updateStatus("Dashboard loaded successfully");
    }
    
    private void loadStatistics() {
        try {
            int totalBooks = bookDAO.getAllBooks().size();
            totalBooksLabel.setText(String.valueOf(totalBooks));
            
            int totalMembers = memberDAO.getAllMembers().size();
            totalMembersLabel.setText(String.valueOf(totalMembers));
            
            int currentlyIssued = issuedBookDAO.getCurrentlyIssuedBooks().size();
            currentlyIssuedLabel.setText(String.valueOf(currentlyIssued));
            
            int overdue = issuedBookDAO.getOverdueBooks().size();
            overdueLabel.setText(String.valueOf(overdue));
            
        } catch (Exception e) {
            showError("Error loading statistics", e.getMessage());
        }
    }
    
    @FXML
    private void showBookManagement() {
        loadScene("views/BookManagement.fxml", "Book Management");
    }
    
    @FXML
    private void showMemberManagement() {
        loadScene("views/MemberManagement.fxml", "Member Management");
    }
    
    @FXML
    private void showIssueBook() {
        loadScene("views/IssueBook.fxml", "Issue Book");
    }
    
    @FXML
    private void showReturnBook() {
        loadScene("views/ReturnBook.fxml", "Return Book");
    }
    
    @FXML
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Library Management System");
        alert.setContentText("Version 1.0.0\n\nA desktop application for managing library operations.\n\nBuilt with JavaFX and MySQL.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleExit() {
        Platform.exit();
    }
    
    private void loadScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
            Parent root = loader.load();
            
            Stage stage = (Stage) totalBooksLabel.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/style.css").toExternalForm());
            
            stage.setScene(scene);
            stage.setTitle(title);
            
        } catch (IOException e) {
            showError("Error loading screen", "Could not load " + fxmlFile + "\n" + e.getMessage());
        }
    }
    
    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
