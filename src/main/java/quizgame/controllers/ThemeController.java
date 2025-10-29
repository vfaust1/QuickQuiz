
package quizgame.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.util.StringConverter;
import javafx.scene.input.ScrollEvent;

public class ThemeController {
    @FXML
    private javafx.scene.control.Label labelErreurNbQuestions;
    
    @FXML
    private Button btnCinema;
    
    @FXML
    private Button btnFootball;
    
    @FXML
    private Button btnJeuxVideo;
    
    @FXML
    private Button btnManga;
    
    @FXML
    private Button btnMusique;
    
    @FXML
    private Button btnSeriesTV;
    
    @FXML
    private Button btnAnimation;
    
    @FXML
    private Button btnBandeDessinee;
    
    @FXML
    private Button btnAleatoire;
    

    @FXML
    private Button btnRetour;

    @FXML
    private Spinner<Integer> spinnerNbQuestions;

    @FXML
    private javafx.scene.layout.HBox hboxNbQuestions;
    
    private String nomJoueur;
    private int nbQuestions;
    private boolean modeChrono = false;
    private String themeSelectionne = null;
    private Button dernierBoutonSelectionne = null;
    
    @FXML
    private void initialize() {
        // Spinner pour le nombre de questions (1 à 50, valeur par défaut 10)
        if (spinnerNbQuestions != null) {
            // Rendre le spinner éditable au clavier
            spinnerNbQuestions.setEditable(true);

            SpinnerValueFactory.IntegerSpinnerValueFactory vf =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 10);

            // Converter pour parser/clamp le texte saisi
            vf.setConverter(new StringConverter<Integer>() {
                @Override
                public String toString(Integer value) {
                    return value == null ? "" : Integer.toString(value);
                }

                @Override
                public Integer fromString(String text) {
                    if (text == null) throw new NumberFormatException("null");
                    String t = text.trim();
                    if (t.isEmpty()) throw new NumberFormatException("empty");
                    int v = Integer.parseInt(t);
                    // Clamp aux bornes
                    if (v < vf.getMin()) v = vf.getMin();
                    if (v > vf.getMax()) v = vf.getMax();
                    return v;
                }
            });

            spinnerNbQuestions.setValueFactory(vf);

            // Style de l'éditeur
            spinnerNbQuestions.getEditor().setStyle("-fx-font-style: normal; -fx-text-fill: white;");
            spinnerNbQuestions.valueProperty().addListener((obs, oldVal, newVal) -> {
                spinnerNbQuestions.getEditor().setStyle("-fx-font-style: normal; -fx-text-fill: white;");
                if (labelErreurNbQuestions != null) labelErreurNbQuestions.setVisible(false);
            });

            // Commit de la valeur saisie sur Enter et perte de focus
            Runnable commitEditor = () -> {
                try {
                    int v = vf.getConverter().fromString(spinnerNbQuestions.getEditor().getText());
                    vf.setValue(v);
                    spinnerNbQuestions.getEditor().setText(Integer.toString(v));
                    if (labelErreurNbQuestions != null) labelErreurNbQuestions.setVisible(false);
                } catch (Exception ex) {
                    if (labelErreurNbQuestions != null) {
                        labelErreurNbQuestions.setText("Veuillez entrer un nombre valide (1-50)");
                        labelErreurNbQuestions.setVisible(true);
                    }
                }
            };
            spinnerNbQuestions.getEditor().setOnAction(e -> commitEditor.run());
            spinnerNbQuestions.focusedProperty().addListener((o, was, isNow) -> { if (!isNow) commitEditor.run(); });

            // Molette souris pour incrémenter/décrémenter
            spinnerNbQuestions.addEventFilter(ScrollEvent.SCROLL, e -> {
                if (e.getDeltaY() > 0) spinnerNbQuestions.increment();
                else spinnerNbQuestions.decrement();
            });

            // Valeur par défaut dans nbQuestions
            nbQuestions = vf.getValue();
        }

            // Ajouter les gestionnaires de double-clic pour tous les boutons de thème
            ajouterDoubleClic(btnCinema, "cinema");
            ajouterDoubleClic(btnFootball, "football"); 
            ajouterDoubleClic(btnJeuxVideo, "jeuxvideo");
            ajouterDoubleClic(btnManga, "manga");
            ajouterDoubleClic(btnMusique, "musique");
            ajouterDoubleClic(btnSeriesTV, "seriestv");
            ajouterDoubleClic(btnAnimation, "animation");
            ajouterDoubleClic(btnBandeDessinee, "bandedessinee");
            ajouterDoubleClic(btnAleatoire, "Aleatoire");
        }

        
    
        private void ajouterDoubleClic(Button bouton, String theme) {
            bouton.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    // Double-clic : sélectionner et valider immédiatement
                    selectionnerTheme(theme, bouton);
                lancerPartie(theme);
            }
        });
    }
    
    public void setJoueurData(String nomJoueur, int nbQuestions, boolean modeChrono) {
        this.nomJoueur = nomJoueur;
        this.nbQuestions = nbQuestions;
        this.modeChrono = modeChrono;
        
        // Cacher le sélecteur de nombre de questions en mode chrono
        if (hboxNbQuestions != null) {
            hboxNbQuestions.setVisible(!modeChrono);
            hboxNbQuestions.setManaged(!modeChrono);
        }
    }
    
    @FXML
    private void onRetourClick() {
        // Retourner à la page de choix du mode avec les données du joueur
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ModeSelectionController.fxml"));
            
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
            
            // Passer le nom du joueur au ModeSelectionController pour qu'il reste connecté
            ModeSelectionController modeController = fxmlLoader.getController();
            if (modeController != null && nomJoueur != null) {
                modeController.setJoueurData(nomJoueur);
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
    
    private void lancerPartie(String theme) {
        if (labelErreurNbQuestions != null) {
            labelErreurNbQuestions.setVisible(false);
            labelErreurNbQuestions.setText("");
        }
        
        // En mode chrono, ne pas utiliser le spinner (nombre de questions illimité)
        if (!modeChrono && spinnerNbQuestions != null) {
            try {
                nbQuestions = spinnerNbQuestions.getValue();
            } catch (Exception e) {
                if (labelErreurNbQuestions != null) {
                    labelErreurNbQuestions.setText("Veuillez entrer un nombre");
                    labelErreurNbQuestions.setVisible(true);
                }
                return;
            }
        }
        // En mode chrono, utiliser une valeur par défaut (par exemple 50 pour avoir assez de questions)
        if (modeChrono) {
            nbQuestions = 50;
        }
        // Validation des données
        if (nomJoueur == null || nomJoueur.trim().isEmpty()) {
            showAlert("Erreur", "Données du joueur manquantes !");
            return;
        }
        
        // Validation du nombre de questions seulement en mode classique
        if (!modeChrono && (nbQuestions < 1 || nbQuestions > 50)) {
            showAlert("Erreur", "Nombre de questions invalide !");
            return;
        }
        
        // Lancer le jeu directement
        loadJeuPage(theme);
    }
    
    private void loadJeuPage(String theme) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/JeuController.fxml"));
            
            // Préserver l'état de la fenêtre
            Stage stage = (Stage) btnCinema.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();
            double oldW = stage.getWidth();
            double oldH = stage.getHeight();
            double oldX = stage.getX();
            double oldY = stage.getY();
            
            Scene scene = new Scene(fxmlLoader.load(), oldW, oldH);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            // Placer la scène sur la fenêtre AVANT d'appeler les initialisations
            stage.setScene(scene);

            // Passer les données au contrôleur de jeu (maintenant la scene est attachée au stage)
            JeuController jeuController = fxmlLoader.getController();
            jeuController.initialiserPartie(nomJoueur, theme, nbQuestions, modeChrono);
            
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
    
    @FXML
    private void onCinemaClick() {
        selectionnerTheme("cinema", btnCinema);
    }
    
    @FXML
    private void onFootballClick() {
        selectionnerTheme("football", btnFootball);
    }
    
    @FXML
    private void onJeuxVideoClick() {
        selectionnerTheme("jeuxvideo", btnJeuxVideo);
    }
    
    @FXML
    private void onMangaClick() {
        selectionnerTheme("manga", btnManga);
    }
    
    @FXML
    private void onMusiqueClick() {
        selectionnerTheme("musique", btnMusique);
    }
    
    @FXML
    private void onSeriesTVClick() {
        selectionnerTheme("seriestv", btnSeriesTV); // Utiliser litterature pour Series TV
    }
    
    @FXML
    private void onAnimationClick() {
        selectionnerTheme("animation", btnAnimation);
    }
    
    @FXML
    private void onBandeDessineeClick() {
        selectionnerTheme("bandedessinee", btnBandeDessinee); // Utiliser litterature pour BD
    }
    
    @FXML
    private void onAleatoireClick() {
        selectionnerTheme("aleatoire", btnAleatoire);
    }

        private void selectionnerTheme(String theme, Button bouton) {
            // Désélectionner le bouton précédent
            if (dernierBoutonSelectionne != null) {
                dernierBoutonSelectionne.getStyleClass().remove("theme-selected");
            }
        
            // Sélectionner le nouveau bouton
            themeSelectionne = theme;
            dernierBoutonSelectionne = bouton;
            bouton.getStyleClass().add("theme-selected");
        }

    @FXML
    private void onValiderClick() {
        if (themeSelectionne == null) {
            showAlert("Sélection requise", "Merci de sélectionner un thème avant de valider.");
            return;
        }
        lancerPartie(themeSelectionne);
    }
}
