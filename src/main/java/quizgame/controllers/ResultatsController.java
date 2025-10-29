package quizgame.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class ResultatsController {
    
    @FXML
    private Label lblTitre;
    
    @FXML
    private Label lblScore;
    
    @FXML
    private Label lblTheme;
    
    @FXML
    private Button btnMenuPrincipal;
    
    private String pseudo;
    private String theme;
    private int score;
    private int nbQuestions;
    private boolean modeChrono;
    
    @FXML
    private void initialize() {
        // L'initialisation se fait via setResultats()
    }
    
    public void setResultats(String pseudo, String theme, int score, int nbQuestions) {
        setResultats(pseudo, theme, score, nbQuestions, false);
    }
    
    public void setResultats(String pseudo, String theme, int score, int nbQuestions, boolean modeChrono) {
        this.pseudo = pseudo;
        this.theme = theme;
        this.score = score;
        this.nbQuestions = nbQuestions;
        this.modeChrono = modeChrono;
        
        // Mettre à jour le titre selon le score
        updateTitre();
        
        // Mettre à jour l'affichage selon le mode
        if (modeChrono) {
            lblScore.setText("Vous avez obtenu un score de " + score + " points");
        } else {
            lblScore.setText("Vous avez obtenu un score de " + score + "/" + nbQuestions);
        }
        lblTheme.setText("sur le thème : " + theme);
    }
    
    private void updateTitre() {
        String titre;
        String couleur;
        
        // Cas spécial : 0 bonne réponse
        if (score == 0) {
            titre = "Oui oui allez va dormir";
            couleur = "#B71C1C"; // Rouge foncé
        } else if (modeChrono) {
            // Mode chrono : basé sur le nombre de bonnes réponses
            if (score < 5) {
                titre = "Aïe";
                couleur = "#F44336"; // Rouge
            } else if (score < 10) {
                titre = "Mouais...";
                couleur = "#FF9800"; // Orange
            } else if (score < 15) {
                titre = "Bien joué";
                couleur = "#2196F3"; // Bleu
            } else if (score < 20) {
                titre = "Félicitations";
                couleur = "#4CAF50"; // Vert
            } else {
                titre = "WOW";
                couleur = "#9C27B0"; // Violet
            }
        } else {
            // Mode libre : basé sur le pourcentage
            double pourcentage = (double) score / nbQuestions * 100;
            
            if (pourcentage < 25) {
                titre = "Aïe";
                couleur = "#F44336"; // Rouge
            } else if (pourcentage < 50) {
                titre = "Mouais...";
                couleur = "#FF9800"; // Orange
            } else if (pourcentage < 75) {
                titre = "Bien joué";
                couleur = "#2196F3"; // Bleu
            } else if (pourcentage < 100) {
                titre = "Félicitations";
                couleur = "#4CAF50"; // Vert
            } else {
                titre = "WOW";
                couleur = "#9C27B0"; // Violet
            }
        }
        
        lblTitre.setText(titre);
        
        // Appliquer un effet multicolore pour "WOW"
        if (titre.equals("WOW")) {
            lblTitre.setStyle("-fx-text-fill: linear-gradient(to right, #FF0080, #FF8C00, #40E0D0, #9C27B0); " +
                             "-fx-font-size: 60px; " +
                             "-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.8), 5, 0.5, 0, 0);");
        } else {
            lblTitre.setStyle("-fx-text-fill: " + couleur + ";");
        }
    }
    
    @FXML
    private void onMenuPrincipalClick() {
        // Retourner au menu principal avec le nom du joueur
        loadMenuPage();
    }
    
    private void loadMenuPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MenuController.fxml"));
            
                // Préserver l'état de la fenêtre
                Stage stage = (Stage) btnMenuPrincipal.getScene().getWindow();
                boolean wasFullScreen = stage.isFullScreen();
                boolean wasMaximized = stage.isMaximized();
                double oldW = stage.getWidth();
                double oldH = stage.getHeight();
                double oldX = stage.getX();
                double oldY = stage.getY();
            
                Scene scene = new Scene(loader.load(), oldW, oldH);
            
            // Passer le nom du joueur au MenuController pour qu'il reste connecté
            MenuController menuController = loader.getController();
            if (menuController != null && pseudo != null) {
                menuController.setNomJoueur(pseudo);
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
            System.err.println("Impossible de charger le menu principal : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
