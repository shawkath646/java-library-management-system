package utils;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class KeyboardShortcutUtil {
    
    private static boolean isFullScreen = false;
    
    public static void applyShortcuts(Scene scene, Stage stage) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F11) {
                toggleFullScreen(stage);
                event.consume();
            }
            
            if (event.getCode() == KeyCode.ESCAPE) {
                if (stage.isFullScreen()) {
                    stage.setFullScreen(false);
                    isFullScreen = false;
                    event.consume();
                }
            }
            
            if (event.getCode() == KeyCode.F5) {
                refreshCurrentView(scene);
                event.consume();
            }
            
            if (event.isControlDown() && event.getCode() == KeyCode.Q) {
                showExitConfirmation(stage);
                event.consume();
            }
            
            if (event.isControlDown() && event.getCode() == KeyCode.F) {
                toggleFullScreen(stage);
                event.consume();
            }
            
            if (event.isAltDown() && event.getCode() == KeyCode.F4) {
                showExitConfirmation(stage);
                event.consume();
            }
        });
        
        stage.setFullScreen(isFullScreen);
    }
    
    private static void toggleFullScreen(Stage stage) {
        isFullScreen = !isFullScreen;
        stage.setFullScreen(isFullScreen);
        
        if (isFullScreen) {
            System.out.println("Fullscreen mode enabled. Press F11 or ESC to exit.");
        } else {
            System.out.println("Fullscreen mode disabled.");
        }
    }
    
    private static void refreshCurrentView(Scene scene) {
        System.out.println("Refreshing current view...");
    }
    
    private static void showExitConfirmation(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to exit or Cancel to stay.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                stage.close();
            }
        });
    }
}
