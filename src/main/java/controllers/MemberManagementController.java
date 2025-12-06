package controllers;

import dao.MemberDAO;
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
import models.Member;
import utils.ValidationUtil;

import java.io.IOException;
import java.util.Optional;

public class MemberManagementController {
    
    @FXML
    private TextField searchField;
    
    @FXML
    private TableView<Member> membersTable;
    
    @FXML
    private Button editButton;
    
    @FXML
    private Button deleteButton;
    
    private MemberDAO memberDAO;
    private ObservableList<Member> membersList;
    
    @FXML
    public void initialize() {
        memberDAO = new MemberDAO();
        membersList = FXCollections.observableArrayList();
        membersTable.setItems(membersList);
        
        membersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        membersTable.getSortOrder().clear();
        
        membersTable.setOnMouseClicked(event -> {
            if (event.getTarget() == membersTable || event.getPickResult().getIntersectedNode() == null) {
                membersTable.getSelectionModel().clearSelection();
            }
        });
        
        editButton.disableProperty().bind(
            membersTable.getSelectionModel().selectedItemProperty().isNull()
        );
        deleteButton.disableProperty().bind(
            membersTable.getSelectionModel().selectedItemProperty().isNull()
        );
        
        loadAllMembers();
        setupKeyboardShortcuts();
    }
    
    private void setupKeyboardShortcuts() {
        membersTable.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case N:
                    if (event.isControlDown()) {
                        handleAddMember();
                        event.consume();
                    }
                    break;
                case F:
                    if (event.isControlDown()) {
                        searchField.requestFocus();
                        event.consume();
                    }
                    break;
                case F5:
                    handleRefresh();
                    event.consume();
                    break;
                default:
                    break;
            }
        });
    }
    
    private void loadAllMembers() {
        membersList.clear();
        membersList.addAll(memberDAO.getAllMembers());
    }
    
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadAllMembers();
        } else {
            membersList.clear();
            membersList.addAll(memberDAO.searchMembers(searchTerm));
        }
    }
    
    @FXML
    private void handleAddMember() {
        Dialog<Member> dialog = createMemberDialog("Add New Member", null);
        
        Optional<Member> result = dialog.showAndWait();
        result.ifPresent(member -> {
            if (memberDAO.addMember(member)) {
                showSuccess("Member added successfully!");
                loadAllMembers();
            } else {
                showError("Error adding member", "Could not add the member to the database.");
            }
        });
    }
    
    @FXML
    private void handleEditMember() {
        Member selectedMember = membersTable.getSelectionModel().getSelectedItem();
        
        if (selectedMember == null) {
            showWarning("No member selected", "Please select a member to edit.");
            return;
        }
        
        Dialog<Member> dialog = createMemberDialog("Edit Member", selectedMember);
        
        Optional<Member> result = dialog.showAndWait();
        result.ifPresent(member -> {
            member.setMemberId(selectedMember.getMemberId());
            if (memberDAO.updateMember(member)) {
                showSuccess("Member updated successfully!");
                loadAllMembers();
            } else {
                showError("Error updating member", "Could not update the member in the database.");
            }
        });
    }
    
    @FXML
    private void handleDeleteMember() {
        Member selectedMember = membersTable.getSelectionModel().getSelectedItem();
        
        if (selectedMember == null) {
            showWarning("No member selected", "Please select a member to delete.");
            return;
        }
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Member");
        confirmDialog.setContentText("Are you sure you want to delete \"" + selectedMember.getName() + "\"?");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (memberDAO.deleteMember(selectedMember.getMemberId())) {
                showSuccess("Member deleted successfully!");
                loadAllMembers();
            } else {
                showError("Error deleting member", "Could not delete the member from the database.");
            }
        }
    }
    
    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadAllMembers();
    }
    
    @FXML
    private void handleBackToDashboard() {
        loadDashboard();
    }
    
    private Dialog<Member> createMemberDialog(String title, Member member) {
        Dialog<Member> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(member == null ? "Enter member details" : "Edit member details");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(30, 30, 30, 30));
        grid.setMinWidth(500);
        
        TextField nameField = new TextField(member != null ? member.getName() : "");
        TextField emailField = new TextField(member != null ? member.getEmail() : "");
        TextField phoneField = new TextField(member != null ? member.getPhone() : "");
        TextField addressField = new TextField(member != null ? member.getAddress() : "");
        
        nameField.setPromptText("Name");
        nameField.setPrefWidth(350);
        emailField.setPromptText("Email");
        emailField.setPrefWidth(350);
        phoneField.setPromptText("Phone");
        phoneField.setPrefWidth(350);
        addressField.setPromptText("Address");
        addressField.setPrefWidth(350);
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (ValidationUtil.isEmpty(nameField.getText())) {
                    showWarning("Validation Error", "Name is required!");
                    return null;
                }
                
                if (!ValidationUtil.isValidEmail(emailField.getText())) {
                    showWarning("Validation Error", "Please enter a valid email address!");
                    return null;
                }
                
                if (!ValidationUtil.isValidPhone(phoneField.getText())) {
                    showWarning("Validation Error", "Please enter a valid phone number!");
                    return null;
                }
                
                return new Member(
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim()
                );
            }
            return null;
        });
        
        return dialog;
    }
    
    private void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/Dashboard.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) membersTable.getScene().getWindow();
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
