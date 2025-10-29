// Le fichier doit déclarer le package "quizgame" car il est associé au module nommé 'quizgame'.
package quizgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.IOException;

public class QuizApplication extends Application {
    
    private static Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Charger directement la page Welcome
        FXMLLoader fxmlLoader = new FXMLLoader(QuizApplication.class.getResource("/fxml/WelcomeController.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // Appliquer le style CSS
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        // Configuration de la fenêtre
        stage.setTitle("QuickQuiz");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setMaximized(true);
        stage.centerOnScreen();

        // Toggle fullscreen with F11
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });

        // Icône de l'application
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/icon.png")));
        } catch (Exception e) {
            // Si l'icône n'existe pas, continuer sans
        }

        stage.show();
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        launch();
    }
}
