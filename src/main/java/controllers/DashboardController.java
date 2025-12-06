package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.BookDAO;
import dao.IssuedBookDAO;
import dao.MemberDAO;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Book;
import models.Member;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardController {
    
    @FXML
    private Label totalBooksLabel;
    
    @FXML
    private Label availableBooksLabel;
    
    @FXML
    private Label totalMembersLabel;
    
    @FXML
    private Label currentlyIssuedLabel;
    
    @FXML
    private Label overdueLabel;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Label userLabel;
    
    @FXML
    private Label dateTimeLabel;
    
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private IssuedBookDAO issuedBookDAO;
    
    @FXML
    public void initialize() {
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
        issuedBookDAO = new IssuedBookDAO();
        
        loadStatistics();
        startDateTimeClock();
        updateStatus("Dashboard loaded successfully");
    }
    
    private void loadStatistics() {
        try {
            int totalBooks = bookDAO.getAllBooks().size();
            totalBooksLabel.setText(String.valueOf(totalBooks));
            
            int currentlyIssued = issuedBookDAO.getCurrentlyIssuedBooks().size();
            currentlyIssuedLabel.setText(String.valueOf(currentlyIssued));
            
            int availableBooks = (int) bookDAO.getAllBooks().stream()
                .filter(book -> book.getQuantity() > 0)
                .count();
            availableBooksLabel.setText(String.valueOf(availableBooks));
            
            int totalMembers = memberDAO.getAllMembers().size();
            totalMembersLabel.setText(String.valueOf(totalMembers));
            
            int overdue = issuedBookDAO.getOverdueBooks().size();
            overdueLabel.setText(String.valueOf(overdue));
            
        } catch (Exception e) {
            showError("Error loading statistics", e.getMessage());
        }
    }
    
    private void startDateTimeClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm:ss a");
            dateTimeLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
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
            Scene scene = stage.getScene();
            
            scene.setRoot(root);
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
    
    @FXML
    private void handleImportBooks() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Books from JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        
        File file = fileChooser.showOpenDialog(totalBooksLabel.getScene().getWindow());
        if (file != null) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Book[] books = gson.fromJson(reader, Book[].class);
                
                int imported = 0;
                for (Book book : books) {
                    if (bookDAO.addBook(book)) {
                        imported++;
                    }
                }
                
                loadStatistics();
                showSuccess("Import Successful", imported + " books imported successfully!");
                updateStatus("Imported " + imported + " books");
            } catch (Exception e) {
                showError("Import Error", "Failed to import books: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleImportMembers() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Members from JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        
        File file = fileChooser.showOpenDialog(totalBooksLabel.getScene().getWindow());
        if (file != null) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Member[] members = gson.fromJson(reader, Member[].class);
                
                int imported = 0;
                for (Member member : members) {
                    if (memberDAO.addMember(member)) {
                        imported++;
                    }
                }
                
                loadStatistics();
                showSuccess("Import Successful", imported + " members imported successfully!");
                updateStatus("Imported " + imported + " members");
            } catch (Exception e) {
                showError("Import Error", "Failed to import members: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleExportBooks() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Books to JSON");
        fileChooser.setInitialFileName("books_export.json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        
        File file = fileChooser.showSaveDialog(totalBooksLabel.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                List<Book> books = bookDAO.getAllBooks();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(books, writer);
                
                showSuccess("Export Successful", books.size() + " books exported successfully!");
                updateStatus("Exported " + books.size() + " books");
            } catch (Exception e) {
                showError("Export Error", "Failed to export books: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleExportMembers() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Members to JSON");
        fileChooser.setInitialFileName("members_export.json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        
        File file = fileChooser.showSaveDialog(totalBooksLabel.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                List<Member> members = memberDAO.getAllMembers();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(members, writer);
                
                showSuccess("Export Successful", members.size() + " members exported successfully!");
                updateStatus("Exported " + members.size() + " members");
            } catch (Exception e) {
                showError("Export Error", "Failed to export members: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleExportPDFReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Library Report to PDF");
        fileChooser.setInitialFileName("library_report.pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        
        File file = fileChooser.showSaveDialog(totalBooksLabel.getScene().getWindow());
        if (file != null) {
            try {
                generatePDFReport(file);
                showSuccess("Export Successful", "Library report exported successfully!");
                updateStatus("PDF report generated");
            } catch (Exception e) {
                showError("Export Error", "Failed to generate PDF report: " + e.getMessage());
            }
        }
    }
    
    private void generatePDFReport(File file) throws Exception {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(file));
        document.open();
        
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("Library Management System - Report", titleFont);
        title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        document.add(title);
        
        document.add(new com.itextpdf.text.Paragraph("\n"));
        document.add(new com.itextpdf.text.Paragraph("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a"))));
        document.add(new com.itextpdf.text.Paragraph("\n"));
        
        com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);
        document.add(new com.itextpdf.text.Paragraph("Library Statistics", headerFont));
        document.add(new com.itextpdf.text.Paragraph("\n"));
        
        document.add(new com.itextpdf.text.Paragraph("Total Books: " + totalBooksLabel.getText()));
        document.add(new com.itextpdf.text.Paragraph("Available Books: " + availableBooksLabel.getText()));
        document.add(new com.itextpdf.text.Paragraph("Total Members: " + totalMembersLabel.getText()));
        document.add(new com.itextpdf.text.Paragraph("Currently Issued: " + currentlyIssuedLabel.getText()));
        document.add(new com.itextpdf.text.Paragraph("Overdue Books: " + overdueLabel.getText()));
        document.add(new com.itextpdf.text.Paragraph("\n"));
        
        document.add(new com.itextpdf.text.Paragraph("Books Inventory", headerFont));
        document.add(new com.itextpdf.text.Paragraph("\n"));
        
        com.itextpdf.text.pdf.PdfPTable booksTable = new com.itextpdf.text.pdf.PdfPTable(5);
        booksTable.setWidthPercentage(100);
        booksTable.addCell("Book ID");
        booksTable.addCell("Title");
        booksTable.addCell("Author");
        booksTable.addCell("Category");
        booksTable.addCell("Quantity");
        
        for (Book book : bookDAO.getAllBooks()) {
            booksTable.addCell(String.valueOf(book.getBookId()));
            booksTable.addCell(book.getTitle());
            booksTable.addCell(book.getAuthor());
            booksTable.addCell(book.getCategory());
            booksTable.addCell(String.valueOf(book.getQuantity()));
        }
        document.add(booksTable);
        
        document.add(new com.itextpdf.text.Paragraph("\n"));
        
        document.add(new com.itextpdf.text.Paragraph("Members List", headerFont));
        document.add(new com.itextpdf.text.Paragraph("\n"));
        
        com.itextpdf.text.pdf.PdfPTable membersTable = new com.itextpdf.text.pdf.PdfPTable(4);
        membersTable.setWidthPercentage(100);
        membersTable.addCell("Member ID");
        membersTable.addCell("Name");
        membersTable.addCell("Email");
        membersTable.addCell("Phone");
        
        for (Member member : memberDAO.getAllMembers()) {
            membersTable.addCell(String.valueOf(member.getMemberId()));
            membersTable.addCell(member.getName());
            membersTable.addCell(member.getEmail());
            membersTable.addCell(member.getPhone());
        }
        document.add(membersTable);
        
        document.close();
    }
    
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
