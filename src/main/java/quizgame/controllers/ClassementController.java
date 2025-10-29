package quizgame.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import quizgame.Classement;

public class ClassementController {
    
    @FXML
    private Button btnRetour;
    
    @FXML
    private Button btnMode;
    
    @FXML
    private Button btnTheme;
    
    @FXML
    private ScrollPane scrollPane;
    
    @FXML
    private VBox vboxClassement;
    
    @FXML
    private Label lblTitre;
    
    @FXML
    private Label lblJoueur1;
    
    @FXML
    private Label lblJoueur2;
    
    @FXML
    private Label lblJoueur3;
    
    @FXML
    private Label lblScore1;
    
    @FXML
    private Label lblScore2;
    
    @FXML
    private Label lblScore3;
    
    private String filtreTheme = null; // null = tous les thèmes
    private String filtreMode = "libre"; // "libre" par défaut, ou "chrono"
    
    @FXML
    private void initialize() {
        // lblTitre n'existe plus dans le FXML (remplacé par l'image)
        if (lblTitre != null) {
            lblTitre.setText("Classement");
        }
        afficherClassement();
        afficherPodium();
    }
    
    private void afficherPodium() {
        try {
            // Charger le fichier selon le mode sélectionné
            String fichier = filtreMode.equals("chrono") ? "txt/classement_chrono.txt" : "txt/classement_libre.txt";
            List<String[]> scores = Classement.chargerClassement(fichier);
            
            if (scores.isEmpty()) {
                lblJoueur1.setText("Aucun joueur");
                lblJoueur2.setText("Aucun joueur");
                lblJoueur3.setText("Aucun joueur");
                lblScore1.setText("0/0");
                lblScore2.setText("0/0");
                lblScore3.setText("0/0");
                return;
            }
            
            // Appliquer le filtre par thème si nécessaire
            if (filtreTheme != null) {
                scores = scores.stream()
                    .filter(score -> score[1].equalsIgnoreCase(filtreTheme))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            if (scores.isEmpty()) {
                lblJoueur1.setText("Aucun score");
                lblJoueur2.setText("pour ce thème");
                lblJoueur3.setText("");
                lblScore1.setText("");
                lblScore2.setText("");
                lblScore3.setText("");
                return;
            }
            
            // Trier les scores
            scores.sort((a, b) -> {
                // Déterminer les indices selon le format (5 ou 6 colonnes)
                int scoreIndexA = (a.length == 6) ? 3 : 2;
                int scoreIndexB = (b.length == 6) ? 3 : 2;
                int nbQuestionsIndexA = (a.length == 6) ? 4 : 3;
                int nbQuestionsIndexB = (b.length == 6) ? 4 : 3;
                
                // Récupérer les scores
                int scoreA = Integer.parseInt(a[scoreIndexA]);
                int scoreB = Integer.parseInt(b[scoreIndexB]);
                
                // Pour le mode chrono (nbQuestions = "-"), trier uniquement par score
                boolean isChronoA = a[nbQuestionsIndexA].equals("-");
                boolean isChronoB = b[nbQuestionsIndexB].equals("-");
                
                if (isChronoA && isChronoB) {
                    return Integer.compare(scoreB, scoreA);
                } else if (isChronoA) {
                    return -1;
                } else if (isChronoB) {
                    return 1;
                } else {
                    double ratioA = (double) scoreA / Double.parseDouble(a[nbQuestionsIndexA]);
                    double ratioB = (double) scoreB / Double.parseDouble(b[nbQuestionsIndexB]);
                    int ratioComparison = Double.compare(ratioB, ratioA);
                    if (ratioComparison != 0) {
                        return ratioComparison;
                    }
                    int nbQuestionsA = Integer.parseInt(a[nbQuestionsIndexA]);
                    int nbQuestionsB = Integer.parseInt(b[nbQuestionsIndexB]);
                    return Integer.compare(nbQuestionsB, nbQuestionsA);
                }
            });
            
            // Réinitialiser tous les labels d'abord
            lblJoueur1.setText("-");
            lblScore1.setText("");
            lblJoueur2.setText("-");
            lblScore2.setText("");
            lblJoueur3.setText("-");
            lblScore3.setText("");
            
            // Afficher le podium
            if (scores.size() >= 1) {
                String[] premier = scores.get(0);
                int scoreIndex1 = (premier.length == 6) ? 3 : 2;
                int nbQuestionsIndex1 = (premier.length == 6) ? 4 : 3;
                lblJoueur1.setText(premier[0]);
                if (premier[nbQuestionsIndex1].equals("-")) {
                    lblScore1.setText(premier[scoreIndex1] + " pts");
                } else {
                    lblScore1.setText(premier[scoreIndex1] + "/" + premier[nbQuestionsIndex1]);
                }
            }
            
            if (scores.size() >= 2) {
                String[] deuxieme = scores.get(1);
                int scoreIndex2 = (deuxieme.length == 6) ? 3 : 2;
                int nbQuestionsIndex2 = (deuxieme.length == 6) ? 4 : 3;
                lblJoueur2.setText(deuxieme[0]);
                if (deuxieme[nbQuestionsIndex2].equals("-")) {
                    lblScore2.setText(deuxieme[scoreIndex2] + " pts");
                } else {
                    lblScore2.setText(deuxieme[scoreIndex2] + "/" + deuxieme[nbQuestionsIndex2]);
                }
            }
            
            if (scores.size() >= 3) {
                String[] troisieme = scores.get(2);
                int scoreIndex3 = (troisieme.length == 6) ? 3 : 2;
                int nbQuestionsIndex3 = (troisieme.length == 6) ? 4 : 3;
                lblJoueur3.setText(troisieme[0]);
                if (troisieme[nbQuestionsIndex3].equals("-")) {
                    lblScore3.setText(troisieme[scoreIndex3] + " pts");
                } else {
                    lblScore3.setText(troisieme[scoreIndex3] + "/" + troisieme[nbQuestionsIndex3]);
                }
            }
            
        } catch (Exception e) {
            lblJoueur1.setText("Erreur");
            lblJoueur2.setText("Erreur");
            lblJoueur3.setText("Erreur");
        }
    }
    
    @FXML
    private void onModeClick() {
        afficherMenuModes();
    }
    
    private void afficherMenuModes() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Filtrer par mode");
        alert.setHeaderText("Choisissez un mode de jeu :");
        
        // Créer des boutons pour chaque mode
        javafx.scene.control.ButtonType btnLibre = new javafx.scene.control.ButtonType("Mode Libre");
        javafx.scene.control.ButtonType btnChrono = new javafx.scene.control.ButtonType("Mode Chrono");
        javafx.scene.control.ButtonType btnAnnuler = new javafx.scene.control.ButtonType("Annuler", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(btnLibre, btnChrono, btnAnnuler);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == btnLibre) {
                filtreMode = "libre";
            } else if (response == btnChrono) {
                filtreMode = "chrono";
            } else {
                return; // Annuler - ne rien faire
            }
            
            // Recharger les classements avec le nouveau filtre
            vboxClassement.getChildren().clear();
            afficherClassement();
            afficherPodium();
        });
    }
    
    @FXML
    private void onThemeClick() {
        // Afficher un menu de sélection de thème
        afficherMenuThemes();
    }
    
    private void afficherMenuThemes() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Filtrer par thème");
        alert.setHeaderText("Choisissez un thème pour filtrer le classement :");
        
        // Créer des boutons pour chaque thème
        javafx.scene.control.ButtonType btnTous = new javafx.scene.control.ButtonType("Tous les thèmes");
        javafx.scene.control.ButtonType btnCinema = new javafx.scene.control.ButtonType("Cinema");
        javafx.scene.control.ButtonType btnFootball = new javafx.scene.control.ButtonType("Football");
        javafx.scene.control.ButtonType btnJeuxVideo = new javafx.scene.control.ButtonType("Jeux Video");
        javafx.scene.control.ButtonType btnManga = new javafx.scene.control.ButtonType("Manga");
        javafx.scene.control.ButtonType btnMusique = new javafx.scene.control.ButtonType("Musique");
        javafx.scene.control.ButtonType btnLitterature = new javafx.scene.control.ButtonType("Littérature");
        javafx.scene.control.ButtonType btnAnnuler = new javafx.scene.control.ButtonType("Annuler", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(btnTous, btnCinema, btnFootball, btnJeuxVideo, btnManga, btnMusique, btnLitterature, btnAnnuler);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == btnTous) {
                filtreTheme = null;
            } else if (response == btnCinema) {
                filtreTheme = "cinema";
            } else if (response == btnFootball) {
                filtreTheme = "football";
            } else if (response == btnJeuxVideo) {
                filtreTheme = "jeuxvideo";
            } else if (response == btnManga) {
                filtreTheme = "manga";
            } else if (response == btnMusique) {
                filtreTheme = "musique";
            } else if (response == btnLitterature) {
                filtreTheme = "litterature";
            }
            // Si Annuler, on ne fait rien
            
            // Rafraîchir l'affichage
            vboxClassement.getChildren().clear();
            afficherClassement();
            afficherPodium();
        });
    }
    
    private void afficherClassement() {
        try {
            // Charger le fichier selon le mode sélectionné
            String fichier = filtreMode.equals("chrono") ? "txt/classement_chrono.txt" : "txt/classement_libre.txt";
            List<String[]> scores = Classement.chargerClassement(fichier);
            
            if (scores.isEmpty()) {
                Label lblVide = new Label("Aucun score enregistré pour le moment.");
                lblVide.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
                vboxClassement.getChildren().add(lblVide);
                return;
            }
            
            // Appliquer le filtre par thème si nécessaire
            if (filtreTheme != null) {
                scores = scores.stream()
                    .filter(score -> score[1].equalsIgnoreCase(filtreTheme))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            if (scores.isEmpty()) {
                Label lblVide = new Label("Aucun score trouvé pour le thème sélectionné.");
                lblVide.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
                vboxClassement.getChildren().add(lblVide);
                return;
            }
            
            // Trier les scores par ratio décroissant, puis par nombre de questions en cas d'égalité
            scores.sort((a, b) -> {
                // Déterminer les indices selon le format (5 ou 6 colonnes)
                int scoreIndexA = (a.length == 6) ? 3 : 2;
                int scoreIndexB = (b.length == 6) ? 3 : 2;
                int nbQuestionsIndexA = (a.length == 6) ? 4 : 3;
                int nbQuestionsIndexB = (b.length == 6) ? 4 : 3;
                
                // Récupérer les scores
                int scoreA = Integer.parseInt(a[scoreIndexA]);
                int scoreB = Integer.parseInt(b[scoreIndexB]);
                
                // Pour le mode chrono (nbQuestions = "-"), trier uniquement par score
                boolean isChronoA = a[nbQuestionsIndexA].equals("-");
                boolean isChronoB = b[nbQuestionsIndexB].equals("-");
                
                if (isChronoA && isChronoB) {
                    return Integer.compare(scoreB, scoreA);
                } else if (isChronoA) {
                    return -1;
                } else if (isChronoB) {
                    return 1;
                } else {
                    double ratioA = (double) scoreA / Double.parseDouble(a[nbQuestionsIndexA]);
                    double ratioB = (double) scoreB / Double.parseDouble(b[nbQuestionsIndexB]);
                    int ratioComparison = Double.compare(ratioB, ratioA);
                    if (ratioComparison != 0) {
                        return ratioComparison;
                    }
                    int nbQuestionsA = Integer.parseInt(a[nbQuestionsIndexA]);
                    int nbQuestionsB = Integer.parseInt(b[nbQuestionsIndexB]);
                    return Integer.compare(nbQuestionsB, nbQuestionsA);
                }
            });
            
            // Afficher du 4ème au 10ème (les 3 premiers sont sur le podium)
            int debut = 3; // Commencer à partir du 4ème (index 3)
            int limite = Math.min(10, scores.size());
            for (int i = debut; i < limite; i++) {
                String[] entry = scores.get(i);
                
                // Déterminer les indices selon le format
                int nameIndex = 0;
                int themeIndex = 1;
                int scoreIndex = (entry.length == 6) ? 3 : 2;
                int nbQuestionsIndex = (entry.length == 6) ? 4 : 3;
                int dateIndex = (entry.length == 6) ? 5 : 4;
                String modeStr = (entry.length == 6) ? " (" + entry[2] + ")" : "";
                
                Label lblScore = new Label();
                
                if (entry[nbQuestionsIndex].equals("-")) {
                    // Mode chrono : afficher uniquement le score
                    lblScore.setText(String.format("%d. %s : %s%s = %s pts - %s",
                        i + 1,
                        entry[nameIndex],
                        entry[themeIndex],
                        modeStr,
                        entry[scoreIndex],
                        entry[dateIndex]
                    ));
                } else {
                    // Mode libre : afficher score/nbQuestions et pourcentage
                    int score = Integer.parseInt(entry[scoreIndex]);
                    int nbQuestions = Integer.parseInt(entry[nbQuestionsIndex]);
                    double ratio = (double) score / nbQuestions;
                    double pourcentage = ratio * 100;
                    
                    lblScore.setText(String.format("%d. %s : %s%s = %s/%s (%.1f%%) - %s",
                        i + 1,
                        entry[nameIndex],
                        entry[themeIndex],
                        modeStr,
                        entry[scoreIndex],
                        entry[nbQuestionsIndex],
                        pourcentage,
                        entry[dateIndex]
                    ));
                }
                
                // Style selon le rang
                if (i == 0) {
                    lblScore.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");
                } else if (i == 1) {
                    lblScore.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #C0C0C0;");
                } else if (i == 2) {
                    lblScore.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #CD7F32;");
                } else {
                    lblScore.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
                }
                
                vboxClassement.getChildren().add(lblScore);
            }
            
        } catch (Exception e) {
            Label lblErreur = new Label("Erreur lors du chargement du classement : " + e.getMessage());
            lblErreur.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF0000;");
            vboxClassement.getChildren().add(lblErreur);
        }
    }
    
    @FXML
    private void onRetourClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MenuController.fxml"));
            
                // Préserver l'état de la fenêtre
                Stage stage = (Stage) btnRetour.getScene().getWindow();
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
}
