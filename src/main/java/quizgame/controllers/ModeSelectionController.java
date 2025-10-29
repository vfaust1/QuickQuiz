package quizgame.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class ModeSelectionController {
    
    @FXML
    private Button btnModeClassique;
    
    @FXML
    private Button btnModeChrono;
    
    @FXML
    private Button btnRetour;
    
    @FXML
    private Button btnRules;
    
    private String nomJoueur;
    private String theme;
    private int nbQuestions;
    private boolean modeChrono;
    
    @FXML
    private void initialize() {
        // Configuration initiale
    }
    
    public void setJoueurData(String nomJoueur) {
        this.nomJoueur = nomJoueur;
        quizgame.ProfilSession.setNomJoueur(nomJoueur);
    }
    
    public void setJoueurData(String nomJoueur, String theme, int nbQuestions, boolean modeChrono) {
        this.nomJoueur = nomJoueur;
        this.theme = theme;
        this.nbQuestions = nbQuestions;
        this.modeChrono = modeChrono;
        quizgame.ProfilSession.setNomJoueur(nomJoueur);
    }
    
    @FXML
    private void onModeClassiqueClick() {
        // Aller à la page de choix du thème en mode classique
        loadThemePage(false);
    }
    
    @FXML
    private void onModeChronoClick() {
        // Aller à la page de choix du thème en mode chrono
        loadThemePage(true);
    }
    
    @FXML
    private void onRetourClick() {
        // Retourner à la page pseudo
        loadPseudoPage();
    }
    
    @FXML
    private void onRulesClick() {
        // Afficher les règles du mode chrono
        Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
        rulesAlert.setTitle("QuickQuiz - Mode Chrono : Règles du jeu");
    rulesAlert.setHeaderText(null);
        rulesAlert.setContentText(
            "🕐 Durée : 1 minute plus ou moins\n\n" +
            "⚡ Fonctionnement :\n\n" +
            "• 1 bonne réponse = +2 sec\n" +
            "• 1 mauvaise réponse = -2 sec\n\n" +
            "   Bonne chance !\n"  
        );
        
        // Définir la fenêtre propriétaire pour centrer automatiquement
        try {
            Stage ownerStage = (Stage) btnRules.getScene().getWindow();
            rulesAlert.initOwner(ownerStage);
        } catch (Exception ignore) {
            // Si ça échoue, continuer sans owner
        }
        
        // Retirer l'icône par défaut de l'Alert
        rulesAlert.setGraphic(null);
        // Appliquer le même fond que les autres pages
        try {
            var dialogPane = rulesAlert.getDialogPane();
            
            // Définir une taille minimale pour afficher tout le contenu
            dialogPane.setMinHeight(220);
            dialogPane.setMinWidth(400);
            dialogPane.setPrefHeight(220);
            dialogPane.setPrefWidth(400);
            
            // Ajoute une classe spécifique pour surcharger le header vert uniquement pour cette alerte
            dialogPane.getStyleClass().add("rules-dialog");
            // Arrière-plan image comme les pages (cover, centré)
            dialogPane.setStyle("-fx-background-image: url('/img/fond.jpg'); -fx-background-size: cover; -fx-background-position: center center; -fx-background-repeat: no-repeat;");
            // Appliquer la feuille de style globale si disponible
            var css = getClass().getResource("/css/style.css");
            if (css != null) {
                dialogPane.getStylesheets().add(css.toExternalForm());
            }
            // Retirer aussi le graphic du DialogPane (selon implémentations)
            dialogPane.setGraphic(null);
            // Styliser le bouton OK en fond blanc
            Node okNode = dialogPane.lookupButton(ButtonType.OK);
            if (okNode != null) {
                okNode.getStyleClass().add("rules-ok");
            }
        } catch (Exception ignore) {
            // Si l'application du style échoue, on ignore et on affiche l'alerte par défaut
        }
        
        rulesAlert.showAndWait();
    }
    
    private void loadNbQuestionsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NbQuestionsController.fxml"));
            
            // Préserver l'état de la fenêtre
            Stage stage = (Stage) btnModeClassique.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();
            double oldW = stage.getWidth();
            double oldH = stage.getHeight();
            double oldX = stage.getX();
            double oldY = stage.getY();
            
            Scene scene = new Scene(loader.load(), oldW, oldH);
            
            // Passer les données au contrôleur
            NbQuestionsController controller = loader.getController();
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
            showAlert("Erreur", "Impossible de charger la page de saisie du nombre de questions.");
            e.printStackTrace();
        }
    }
    
    private void loadThemePage(boolean modeChrono) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThemeController.fxml"));
            
            // Préserver l'état de la fenêtre
            Stage stage = (Stage) btnModeChrono.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();
            double oldW = stage.getWidth();
            double oldH = stage.getHeight();
            double oldX = stage.getX();
            double oldY = stage.getY();
            
            Scene scene = new Scene(loader.load(), oldW, oldH);
            
        // Passer les données au contrôleur des thèmes
            ThemeController themeController = loader.getController();
        // Fallback si le nom n'a pas été injecté correctement
        String joueur = (nomJoueur != null && !nomJoueur.isBlank())
            ? nomJoueur
            : quizgame.ProfilSession.getNomJoueur();
        themeController.setJoueurData(joueur, 0, modeChrono); // 0 questions pour le mode chrono
            
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
    
    private void loadPseudoPage() {
        try {
            // Retourner au menu principal au lieu de la page pseudo
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MenuController.fxml"));
            
            // Préserver l'état de la fenêtre
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();
            double oldW = stage.getWidth();
            double oldH = stage.getHeight();
            double oldX = stage.getX();
            double oldY = stage.getY();
            
            Scene scene = new Scene(loader.load(), oldW, oldH);
            
            // Passer le nom du joueur au MenuController pour qu'il reste connecté
            MenuController menuController = loader.getController();
            if (menuController != null && nomJoueur != null) {
                menuController.setNomJoueur(nomJoueur);
            }
            
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
            showAlert("Erreur", "Impossible de charger la page du menu principal.");
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

