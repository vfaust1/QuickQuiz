package quizgame.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.beans.binding.Bindings;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {
    
    private String nomJoueur;
    
    @FXML
    private Button btnJouer;
    
    @FXML
    private Button btnClassement;
    
    @FXML
    private Button btnSeDeconnecter;
    
    @FXML
    private ImageView imgLogo;
    
    @FXML
    private void initialize() {
        // Liaison: largeur du logo = clamp(180, largeur*0.52, 400)
        btnJouer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                imgLogo.fitWidthProperty().bind(
                    Bindings.min(700,
                        Bindings.max(500,
                            newScene.widthProperty().multiply(0.55)
                        )
                    )
                );
            }
        });
    }
    
    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
        // Persister dans la session globale pour les flux où le passage direct échoue
        quizgame.ProfilSession.setNomJoueur(nomJoueur);
    }
    
    @FXML
    private void onJouerClick() {
        try {
            // Charger directement la scène de sélection de mode (on a déjà le profil)
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ModeSelectionController.fxml"));
            
            // Préserver l'état de la fenêtre
            Stage stage = (Stage) btnJouer.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();
            double oldW = stage.getWidth();
            double oldH = stage.getHeight();
            double oldX = stage.getX();
            double oldY = stage.getY();
            
            Scene scene = new Scene(fxmlLoader.load(), oldW, oldH);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            // Passer le nom du joueur au contrôleur de sélection de mode
            ModeSelectionController controller = fxmlLoader.getController();
            if (nomJoueur != null) {
                controller.setJoueurData(nomJoueur);
            }
            
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
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onClassementClick() {
        try {
            // Charger la scène du classement
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ClassementController.fxml"));
            
                // Préserver l'état de la fenêtre
                Stage stage = (Stage) btnClassement.getScene().getWindow();
                boolean wasFullScreen = stage.isFullScreen();
                boolean wasMaximized = stage.isMaximized();
                double oldW = stage.getWidth();
                double oldH = stage.getHeight();
                double oldX = stage.getX();
                double oldY = stage.getY();
            
                Scene scene = new Scene(fxmlLoader.load(), oldW, oldH);
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
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onSeDeconnecterClick() {
        try {
            // Charger la scène du menu des profils
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ProfilMenu.fxml"));
            
            // Préserver l'état de la fenêtre
            Stage stage = (Stage) btnSeDeconnecter.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();
            double oldW = stage.getWidth();
            double oldH = stage.getHeight();
            double oldX = stage.getX();
            double oldY = stage.getY();
            
            Scene scene = new Scene(fxmlLoader.load(), oldW, oldH);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            
            // Restaurer l'état d'affichage
            if (wasFullScreen) {
                stage.setFullScreen(true);
            }
            if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(oldW);
                stage.setHeight(oldH);
                stage.setX(oldX);
                stage.setY(oldY);
                stage.centerOnScreen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
