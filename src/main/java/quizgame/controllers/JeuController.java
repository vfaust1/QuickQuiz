package quizgame.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import quizgame.Question;
import quizgame.QuestionCsvLoader;
import quizgame.Classement;

public class JeuController {
    private String nomJoueur;
    private String theme;
    private int nbQuestions;
    private boolean modeChrono = false;
    private String mode;
    private long tempsDebut;
    private long tempsRestant;
    private javafx.animation.Timeline chronoTimeline;
    private ArrayList<Question> questions;
    private int questionActuelle = 0;
    private int score = 0;
    private Button boutonSelectionne = null;
    // ...champs @FXML existants...
    @FXML
    private ImageView imgThemeBanner;

    @FXML
    private Label lblQuestion;

    @FXML
    private Label lblNumeroQuestion;

    @FXML
    private Label lblScore;

    @FXML
    private Button btnChoix1;

    @FXML
    private Button btnChoix2;

    @FXML
    private Button btnChoix3;

    @FXML
    private Button btnChoix4;

    @FXML
    private Button btnValider;

    @FXML
    private Button btnQuitter;

    // Double-clic sur les boutons de réponse : sélectionne et valide directement
    @FXML
    private void onChoix1MouseClicked(javafx.scene.input.MouseEvent event) {
        selectionnerBouton(btnChoix1);
        if (event.getClickCount() == 2 && !btnValider.isDisabled()) {
            onValiderClick();
        }
    }

    @FXML
    private void onChoix2MouseClicked(javafx.scene.input.MouseEvent event) {
        selectionnerBouton(btnChoix2);
        if (event.getClickCount() == 2 && !btnValider.isDisabled()) {
            onValiderClick();
        }
    }

    @FXML
    private void onChoix3MouseClicked(javafx.scene.input.MouseEvent event) {
        selectionnerBouton(btnChoix3);
        if (event.getClickCount() == 2 && !btnValider.isDisabled()) {
            onValiderClick();
        }
    }

    @FXML
    private void onChoix4MouseClicked(javafx.scene.input.MouseEvent event) {
        selectionnerBouton(btnChoix4);
        if (event.getClickCount() == 2 && !btnValider.isDisabled()) {
            onValiderClick();
        }
    }
    public void initialiserPartie(String nomJoueur, String theme, int nbQuestions, boolean modeChrono) {
        this.nomJoueur = nomJoueur;
        this.theme = theme;
        this.nbQuestions = nbQuestions;
        this.modeChrono = modeChrono;
        this.mode = modeChrono ? "Chrono" : "Classique";
        
        // Charger les questions
        chargerQuestions();
        
        if (modeChrono) {
            initialiserModeChrono();
        }
        
        // Afficher la première question
        afficherQuestion();
    }
    
    private void initialiserModeChrono() {
        tempsDebut = System.currentTimeMillis();
        tempsRestant = 60000; // 1 minute
        
        // Créer le chronomètre
        chronoTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> {
                long tempsActuel = System.currentTimeMillis();
                tempsRestant = 60000 - (tempsActuel - tempsDebut);
                
                if (tempsRestant <= 0) {
                    finirPartie();
                } else {
                    // Mettre à jour l'affichage du temps
                    lblNumeroQuestion.setText("Temps restant: " + (tempsRestant / 1000) + "s");
                }
            })
        );
        chronoTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        chronoTimeline.play();
    }
    
    private void chargerQuestions() {
        try {
            if (theme.equals("Aleatoire")) {
                questions = QuestionCsvLoader.chargerToutesQuestions();
            } else {
                questions = QuestionCsvLoader.chargerDepuisCsv("csv/" + theme.toLowerCase() + ".csv");
                Collections.shuffle(questions);
            }
            
            // Vérifier que les questions ont été chargées
            if (questions == null || questions.isEmpty()) {
                showAlert("Erreur", "Aucune question trouvée pour le thème : " + theme);
                questions = new ArrayList<>(); // Initialiser avec une liste vide
                return;
            }
            
            // Limiter le nombre de questions
            if (questions.size() > nbQuestions) {
                questions = new ArrayList<>(questions.subList(0, nbQuestions));
            }
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les questions : " + e.getMessage());
            questions = new ArrayList<>(); // Initialiser avec une liste vide
        }
    }
    
    private void afficherQuestion() {
        if (questions == null || questions.isEmpty()) {
            showAlert("Erreur", "Aucune question disponible pour ce thème.");
            retournerAuMenu();
            return;
        }
        
        if (questionActuelle >= questions.size()) {
            finirPartie();
            return;
        }
        
        Question question = questions.get(questionActuelle);
        
        // Afficher le numéro de la question ou le temps restant
        if (modeChrono) {
            long tempsActuel = System.currentTimeMillis();
            tempsRestant = 60000 - (tempsActuel - tempsDebut);
            if (tempsRestant <= 0) {
                finirPartie();
                return;
            }
            lblNumeroQuestion.setText("Temps restant: " + (tempsRestant / 1000) + "s");
        } else {
            lblNumeroQuestion.setText("Question n° " + (questionActuelle + 1) + "/" + questions.size());
        }
        lblQuestion.setText(question.getQuestion());
        
        // Afficher l'image du thème
        String imageName = null;
        switch (theme.toLowerCase()) {
            case "musique":
                imageName = "Musique.png";
                break;
            case "cinema":
                imageName = "Cinema.png";
                break;
            case "jeux vidéo":
            case "jeuxvideo":
                imageName = "JeuxVideo.png";
                break;
            case "manga":
                imageName = "Manga.png";
                break;
            case "football":
                imageName = "Football.png";
                break;
            case "series tv":
            case "seriestv":
            case "séries tv":
                imageName = "SeriesTV.png";
                break;
            case "bande dessinée":
            case "bande-dessinee":
            case "bandedessinee":
                imageName = "BD.png";
                break;
            case "animation":
                imageName = "Animation.png";
                break;
            case "aleatoire":
                imageName = "Aleatoire.png";
                break;
            default:
                imageName = null;
        }
        if (imageName != null) {
            try {
                javafx.scene.image.Image img = new javafx.scene.image.Image(getClass().getResource("/img/" + imageName).toExternalForm());
                imgThemeBanner.setImage(img);
                imgThemeBanner.setVisible(true);
                
                // Appliquer des coins arrondis à l'image
                javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
                clip.setWidth(imgThemeBanner.getFitWidth());
                clip.setHeight(imgThemeBanner.getFitHeight());
                clip.setArcWidth(30);
                clip.setArcHeight(30);
                imgThemeBanner.setClip(clip);
                
            } catch (Exception e) {
                imgThemeBanner.setImage(null);
                imgThemeBanner.setVisible(false);
            }
        } else {
            imgThemeBanner.setImage(null);
            imgThemeBanner.setVisible(false);
        }

        
        // Mettre à jour les choix
        String[] choix = question.getChoices();
        btnChoix1.setText(choix[0]);
        btnChoix2.setText(choix[1]);
        btnChoix3.setText(choix[2]);
        btnChoix4.setText(choix[3]);
        
        // Désélectionner tous les boutons
        boutonSelectionne = null;
        
        // Désactiver le bouton valider jusqu'à ce qu'un choix soit fait
        btnValider.setDisable(true);
        
        // Réactiver les boutons et réinitialiser leur style
        btnChoix1.setDisable(false);
        btnChoix2.setDisable(false);
        btnChoix3.setDisable(false);
        btnChoix4.setDisable(false);
        

        // Réinitialiser le style des boutons (enlever la sélection et la couleur verte)
        btnChoix1.getStyleClass().remove("selected");
        btnChoix2.getStyleClass().remove("selected");
        btnChoix3.getStyleClass().remove("selected");
        btnChoix4.getStyleClass().remove("selected");
        
        // Supprimer le style inline (couleur verte) des boutons
        btnChoix1.setStyle("");
        btnChoix2.setStyle("");
        btnChoix3.setStyle("");
        btnChoix4.setStyle("");
    }


    
    @FXML
    private void onChoixSelectionne() {
        // Cette méthode sera appelée par chaque bouton de choix
        Button source = (Button) ((javafx.event.ActionEvent) 
            javafx.stage.Window.getWindows().get(0).getScene().getRoot().getUserData()).getSource();
        
        // Désélectionner tous les autres boutons
        btnChoix1.getStyleClass().remove("selected");
        btnChoix2.getStyleClass().remove("selected");
        btnChoix3.getStyleClass().remove("selected");
        btnChoix4.getStyleClass().remove("selected");
        
        // Sélectionner le bouton cliqué
        source.getStyleClass().add("selected");
        boutonSelectionne = source;
        
        btnValider.setDisable(false);
    }
    
    // Méthodes spécifiques pour chaque bouton
    @FXML
    private void onChoix1Click() {
        selectionnerBouton(btnChoix1);
    }
    
    @FXML
    private void onChoix2Click() {
        selectionnerBouton(btnChoix2);
    }
    
    @FXML
    private void onChoix3Click() {
        selectionnerBouton(btnChoix3);
    }
    
    @FXML
    private void onChoix4Click() {
        selectionnerBouton(btnChoix4);
    }
    
    private void selectionnerBouton(Button bouton) {
        // Désélectionner tous les boutons
        btnChoix1.getStyleClass().remove("selected");
        btnChoix2.getStyleClass().remove("selected");
        btnChoix3.getStyleClass().remove("selected");
        btnChoix4.getStyleClass().remove("selected");
        
        // Sélectionner le bouton cliqué
        bouton.getStyleClass().add("selected");
        boutonSelectionne = bouton;
        
        btnValider.setDisable(false);
    }
    
    @FXML
    private void onValiderClick() {
        if (boutonSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner une réponse !");
            return;
        }
        
        // Vérifier la réponse
        Question question = questions.get(questionActuelle);
        int reponseSelectionnee = -1;
        
        if (boutonSelectionne == btnChoix1) reponseSelectionnee = 0;
        else if (boutonSelectionne == btnChoix2) reponseSelectionnee = 1;
        else if (boutonSelectionne == btnChoix3) reponseSelectionnee = 2;
        else if (boutonSelectionne == btnChoix4) reponseSelectionnee = 3;
        
        if (reponseSelectionnee == question.getGoodAnswer()) {
            score++;
            if (modeChrono) {
                // +2 secondes pour bonne réponse
                tempsDebut += 2000;
                System.out.println("Correct ! +2 secondes");
            }
        } else {
            if (modeChrono) {
                // -2 secondes pour mauvaise réponse
                tempsDebut -= 2000;
                System.out.println("Incorrect ! -2 secondes");
            }
        }
        
        // Désactiver les boutons
        btnChoix1.setDisable(true);
        btnChoix2.setDisable(true);
        btnChoix3.setDisable(true);
        btnChoix4.setDisable(true);   
        
        // Attendre un peu avant de passer à la question suivante (seulement en mode libre)
        if (!modeChrono) {
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                questionActuelle++;
                afficherQuestion();
            });
        } else {
            // En mode chrono, passer immédiatement à la question suivante
            questionActuelle++;
            afficherQuestion();
        }
    }
    
    @FXML
    private void onAbandonnerClick() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Quitter");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir quitter ?\n\nVotre score actuel sera perdu.");
        
        // Créer des boutons personnalisés avec l'ordre souhaité (Annuler à gauche, Quitter à droite)
        javafx.scene.control.ButtonType btnConfirm = new javafx.scene.control.ButtonType("Quitter", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        javafx.scene.control.ButtonType btnAnnuler = new javafx.scene.control.ButtonType("Annuler", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnAnnuler, btnConfirm);
        
        // Définir la fenêtre propriétaire pour centrer automatiquement
        try {
            Stage ownerStage = (Stage) btnQuitter.getScene().getWindow();
            alert.initOwner(ownerStage);
        } catch (Exception ignore) {
            // Si ça échoue, continuer sans owner
        }
        
        // Retirer l'icône par défaut
        alert.setGraphic(null);
        
        // Appliquer le même style que le pop-up des règles
        try {
            var dialogPane = alert.getDialogPane();
            
            // Définir une taille réduite
            dialogPane.setMinHeight(150);
            dialogPane.setMinWidth(400);
            dialogPane.setPrefHeight(150);
            dialogPane.setPrefWidth(400);
            
            // Ajouter une classe spécifique
            dialogPane.getStyleClass().add("rules-dialog");
            
            // Arrière-plan image
            dialogPane.setStyle("-fx-background-image: url('/img/fond.jpg'); -fx-background-size: cover; -fx-background-position: center center; -fx-background-repeat: no-repeat;");
            
            // Centrer le texte avec un style CSS
            javafx.scene.Node contentNode = dialogPane.lookup(".content");
            if (contentNode != null) {
                contentNode.setStyle("-fx-alignment: center; -fx-text-alignment: center;");
            }
            
            // Appliquer la feuille de style globale
            var css = getClass().getResource("/css/style.css");
            if (css != null) {
                dialogPane.getStylesheets().add(css.toExternalForm());
            }
            
            // Retirer le graphic du DialogPane
            dialogPane.setGraphic(null);
            
            // Styliser les boutons personnalisés
            javafx.scene.Node annulerNode = dialogPane.lookupButton(btnAnnuler);
            if (annulerNode != null) {
                annulerNode.getStyleClass().add("rules-ok");
            }
            javafx.scene.Node confirmNode = dialogPane.lookupButton(btnConfirm);
            if (confirmNode != null) {
                confirmNode.getStyleClass().add("rules-ok");
            }
        } catch (Exception ignore) {
            // Si l'application du style échoue, on continue
        }
        
        alert.showAndWait().ifPresent(response -> {
            if (response == btnConfirm) {
                retournerAuMenu();
            }
        });
    }
    
    private void finirPartie() {
        // Arrêter le chronomètre si en mode chrono
        if (modeChrono && chronoTimeline != null) {
            chronoTimeline.stop();
        }
        
        // Sauvegarder le score dans le fichier approprié selon le mode
        String fichierClassement = modeChrono ? "txt/classement_chrono.txt" : "txt/classement_libre.txt";
        Classement.sauvegarderScore(nomJoueur, theme, mode, score, questions.size(), fichierClassement);
        
        // Charger la page de résultats
        chargerPageResultats();
    }
    
    private void chargerPageResultats() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ResultatsController.fxml"));
            
                // Préserver l'état de la fenêtre
                Stage stage = (Stage) btnValider.getScene().getWindow();
                boolean wasFullScreen = stage.isFullScreen();
                boolean wasMaximized = stage.isMaximized();
                double oldW = stage.getWidth();
                double oldH = stage.getHeight();
                double oldX = stage.getX();
                double oldY = stage.getY();
            
                Scene scene = new Scene(fxmlLoader.load(), oldW, oldH);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            // Passer les données au contrôleur de résultats
            ResultatsController resultatsController = fxmlLoader.getController();
            resultatsController.setResultats(nomJoueur, theme, score, questions.size(), modeChrono);
            
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
    
    private void retournerAuMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MenuController.fxml"));
            
                // Préserver l'état de la fenêtre
                Stage stage = (Stage) btnValider.getScene().getWindow();
                boolean wasFullScreen = stage.isFullScreen();
                boolean wasMaximized = stage.isMaximized();
                double oldW = stage.getWidth();
                double oldH = stage.getHeight();
                double oldX = stage.getX();
                double oldY = stage.getY();
            
                Scene scene = new Scene(fxmlLoader.load(), oldW, oldH);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            // Passer le nom du joueur au MenuController pour qu'il reste connecté
            MenuController menuController = fxmlLoader.getController();
            if (menuController != null && nomJoueur != null) {
                menuController.setNomJoueur(nomJoueur);
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
    
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
