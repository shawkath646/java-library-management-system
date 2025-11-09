import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utils.DBUtil;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            if (!DBUtil.testConnection()) {
                showDatabaseError();
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/Dashboard.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/style.css").toExternalForm());
            
            primaryStage.setTitle("Library Management System");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Application Error", "Could not start the application:\n" + e.getMessage());
        }
    }
    
    private void showDatabaseError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Connection Error");
        alert.setHeaderText("Could not connect to the database");
        alert.setContentText(
            "Please ensure that:\n" +
            "1. MySQL server is running\n" +
            "2. Database 'library_app' exists (run database/schema.sql)\n" +
            "3. Database credentials in DBUtil.java are correct\n\n" +
            "Database URL: " + DBUtil.getDatabaseUrl()
        );
        alert.showAndWait();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
