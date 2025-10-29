package quizgame.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
// removed unused imports

public class WelcomeController {

    @FXML
    private Button btnEntree;

    @FXML
    private ImageView imgLogo;

    @FXML
    private void initialize() {
        btnEntree.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Gestion des touches du clavier sur toute la scène
                newScene.setOnKeyPressed(event -> {
                    naviguerVersMenu();
                });
            }
        });
        
        // S'assurer que le bouton peut recevoir le focus pour les événements clavier
        btnEntree.requestFocus();
    }

    @FXML
    private void onEntreeClick() {
        naviguerVersMenu();
    }
    

    /**
     * Charge et affiche la scène du menu principal.
     */
    private void naviguerVersMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ProfilMenu.fxml"));
            // Récupérer le stage et son état courant avant de changer de scène
            Stage stage = (Stage) btnEntree.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();
            double oldW = stage.getWidth();
            double oldH = stage.getHeight();
            double oldX = stage.getX();
            double oldY = stage.getY();

            Parent root = fxmlLoader.load();
            // Créer la scène avec la même taille que l'ancienne pour éviter le rétrécissement
            Scene scene = new Scene(root, oldW, oldH);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            // ProfilMenuController doesn't need stage injection
            // Restaurer l'état d'affichage
            if (wasFullScreen) {
                stage.setFullScreen(true);
            } else if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                // Ne pas recenter pour éviter le déplacement
                stage.setWidth(oldW);
                stage.setHeight(oldH);
                stage.setX(oldX);
                stage.setY(oldY);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
