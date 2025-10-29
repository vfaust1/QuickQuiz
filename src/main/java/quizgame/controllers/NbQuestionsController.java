package quizgame.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class NbQuestionsController {
    
    @FXML
    private TextField txtNbQuestions;
    
    @FXML
    private Button btnContinuer;
    
    @FXML
    private Button btnRetour;
    
    private String nomJoueur;
    
    @FXML
    private void initialize() {
        // Initialisation par défaut
        txtNbQuestions.setText("10");
    }
    
    public void setJoueurData(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }
    
    @FXML
    private void onContinuerClick() {
        String nbQuestionsText = txtNbQuestions.getText().trim();
        
        try {
            int nbQuestions = Integer.parseInt(nbQuestionsText);
            if (nbQuestions < 1 || nbQuestions > 50) {
                showAlert("Erreur", "Le nombre de questions doit être entre 1 et 50.");
                return;
            }
            
            // Charger la page de sélection des thèmes en mode classique
            loadThemePage(nbQuestions, false);
            
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez saisir un nombre valide pour le nombre de questions.");
        }
    }
    
    @FXML
    private void onRetourClick() {
        // Retourner à la page de sélection de mode
        loadModeSelectionPage();
    }
    
    private void loadThemePage(int nbQuestions, boolean modeChrono) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThemeController.fxml"));
            
                // Préserver l'état de la fenêtre
                Stage stage = (Stage) btnContinuer.getScene().getWindow();
                boolean wasFullScreen = stage.isFullScreen();
                boolean wasMaximized = stage.isMaximized();
                double oldW = stage.getWidth();
                double oldH = stage.getHeight();
                double oldX = stage.getX();
                double oldY = stage.getY();
            
                Scene scene = new Scene(loader.load(), oldW, oldH);
            
            // Passer les données au contrôleur des thèmes
            ThemeController themeController = loader.getController();
            themeController.setJoueurData(nomJoueur, nbQuestions, modeChrono);
            
            // Appliquer le style CSS
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            stage.setScene(scene);
            
                // Restaurer l'état d'affichage
                if (wasFullScreen) {
                    stage.setFullScreen(true);
                } else if (wasMaximized) {
                    stage.setMaximized(true);
                } else {
                    stage.setWidth(oldW);
                    stage.setHeight(oldH);
                    stage.setX(oldX);
                    stage.setY(oldY);
                }
            
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page de sélection des thèmes.");
            e.printStackTrace();
        }
    }
    
    private void loadModeSelectionPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModeSelectionController.fxml"));
            
                // Préserver l'état de la fenêtre
                Stage stage = (Stage) btnRetour.getScene().getWindow();
                boolean wasFullScreen = stage.isFullScreen();
                boolean wasMaximized = stage.isMaximized();
                double oldW = stage.getWidth();
                double oldH = stage.getHeight();
                double oldX = stage.getX();
                double oldY = stage.getY();
            
                Scene scene = new Scene(loader.load(), oldW, oldH);
            
            // Passer les données au contrôleur
            ModeSelectionController controller = loader.getController();
            controller.setJoueurData(nomJoueur);
            
            // Appliquer le style CSS
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            stage.setScene(scene);
            
                // Restaurer l'état d'affichage
                if (wasFullScreen) {
                    stage.setFullScreen(true);
                } else if (wasMaximized) {
                    stage.setMaximized(true);
                } else {
                    stage.setWidth(oldW);
                    stage.setHeight(oldH);
                    stage.setX(oldX);
                    stage.setY(oldY);
                }
            
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page de sélection de mode.");
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

