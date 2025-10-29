package quizgame.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
// removed unused imports
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import quizgame.Classement;
import java.util.*;

public class ProfileSelectionController {
    @FXML
    private ListView<String> profileListView;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addProfileButton;
    @FXML
    private Button validateButton;
    @FXML
    private HBox addProfileBox;
    @FXML
    private TextField newProfileTextField;
    @FXML
    private Button confirmAddButton;
    // ...existing code...

    /**
     * Lit le fichier txt/classement.txt et retourne la liste des noms (colonne 1), uniques et triés.
     */
    private List<String> chargerNomsDepuisClassement() {
        // Aggregate profile names from multiple possible classement sources so the app works
        // whether the project stores classement under txt/, src/main/resources/, src/main/resources/
        // or an external runtime "classement/" directory.
        List<String> allLines = new ArrayList<>();
        // Candidate filesystem paths
        Path[] fsCandidates = new Path[] {
            Paths.get("txt", "classement.txt"),
            Paths.get("target", "classes", "txt", "classement.txt"),
            Paths.get("src", "main", "resources", "classement", "classement_libre.txt"),
            Paths.get("src", "main", "resources", "classement", "classement_chrono.txt"),
            Paths.get("src", "main", "resources", "classement", "classement_libre.txt"),
            Paths.get("src", "main", "resources", "classement", "classement_chrono.txt"),
            Paths.get("classement", "classement_libre.txt"),
            Paths.get("classement", "classement_chrono.txt")
        };

        for (Path p : fsCandidates) {
            try {
                if (Files.exists(p)) {
                    allLines.addAll(Files.readAllLines(p, StandardCharsets.UTF_8));
                }
            } catch (IOException ignored) {
            }
        }

        // Try classpath resources (packaged) as a last resort
        String[] resourceCandidates = new String[] {
            "classement/classement_libre.txt",
            "classement/classement_chrono.txt",
            "txt/classement.txt"
        };
        for (String res : resourceCandidates) {
            try (java.io.InputStream is = getClass().getClassLoader().getResourceAsStream(res)) {
                if (is != null) {
                    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(is, StandardCharsets.UTF_8))) {
                        String ln;
                        while ((ln = br.readLine()) != null) {
                            allLines.add(ln);
                        }
                    }
                }
            } catch (IOException ignored) {
            }
        }

        if (allLines.isEmpty()) return Collections.emptyList();

        Map<String, String> uniques = new LinkedHashMap<>();
        for (String line : allLines) {
            if (line == null || line.isBlank()) continue;
            String[] parts = line.split(";", 2);
            if (parts.length >= 1) {
                String raw = parts[0].trim();
                if (!raw.isEmpty()) {
                    String key = raw.toLowerCase(Locale.ROOT);
                    uniques.putIfAbsent(key, raw);
                }
            }
        }
        List<String> names = new ArrayList<>(uniques.values());
        names.sort(Comparator.comparing(s -> s.toLowerCase(Locale.ROOT)));
        return names;
    }
    private ObservableList<String> profiles = FXCollections.observableArrayList();
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Charger les profils depuis classement.txt
        List<String> noms = chargerNomsDepuisClassement();
        profiles.setAll(noms);
        profileListView.setItems(profiles);

        profileListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                validateSelection();
            }
        });
        profileListView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validateSelection();
            }
        });
        validateButton.setOnAction(this::onValidate);
        cancelButton.setOnAction(this::onCancel);
        addProfileButton.setOnAction(this::onAddProfile);
        confirmAddButton.setOnAction(this::onConfirmAddProfile);
        
        // Ajouter un gestionnaire d'événement pour la touche Entrée sur le TextField
        newProfileTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onConfirmAddProfile(new ActionEvent());
            }
        });
    }

    private void validateSelection() {
        String selected = profileListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Sauvegarder le profil sélectionné (vous pouvez créer une classe Session si nécessaire)
            // ProfilSession.setProfilActuel(selected);
            
            // Naviguer vers le menu principal
            try {
                if (stage == null && validateButton.getScene() != null) {
                    stage = (Stage) validateButton.getScene().getWindow();
                }
                if (stage != null) {
                    boolean wasFull = stage.isFullScreen();
                    boolean wasMax = stage.isMaximized();
                    double w = stage.getWidth();
                    double h = stage.getHeight();
                    double x = stage.getX();
                    double y = stage.getY();

                    var loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/MenuController.fxml"));
                    javafx.scene.Parent root = loader.load();
                    
                    // Passer le nom du joueur au MenuController
                    MenuController controller = loader.getController();
                    if (controller != null) {
                        controller.setNomJoueur(selected);
                    }
                    
                    var scene = new javafx.scene.Scene(root, w, h);
                    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                    stage.setScene(scene);

                    if (wasFull) {
                        stage.setFullScreen(true);
                    } else if (wasMax) {
                        stage.setMaximized(true);
                    } else {
                        stage.setX(x);
                        stage.setY(y);
                        stage.setWidth(w);
                        stage.setHeight(h);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void onValidate(ActionEvent event) {
        validateSelection();
    }

    private void onCancel(ActionEvent event) {
        try {
            if (stage == null && cancelButton.getScene() != null) {
                stage = (Stage) cancelButton.getScene().getWindow();
            }
            if (stage != null) {
                boolean wasFull = stage.isFullScreen();
                boolean wasMax = stage.isMaximized();
                double w = stage.getWidth();
                double h = stage.getHeight();
                double x = stage.getX();
                double y = stage.getY();

                var loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/ProfilMenu.fxml"));
                javafx.scene.Parent root = loader.load();
                var scene = new javafx.scene.Scene(root, w, h);
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                stage.setScene(scene);

                if (wasFull) {
                    stage.setFullScreen(true);
                } else if (wasMax) {
                    stage.setMaximized(true);
                } else {
                    stage.setX(x);
                    stage.setY(y);
                    stage.setWidth(w);
                    stage.setHeight(h);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void onAddProfile(ActionEvent event) {
        boolean isVisible = addProfileBox.isVisible();
        addProfileBox.setVisible(!isVisible);
        addProfileBox.setManaged(!isVisible);
    }

    private void onConfirmAddProfile(ActionEvent event) {
        String name = newProfileTextField.getText();
        String trimmed = name.trim();
        if (!trimmed.isEmpty() && !profiles.contains(trimmed)) {
            profiles.add(trimmed);

            // Ajouter le nouveau profil en utilisant la classe `Classement` pour gérer l'emplacement
            try {
                // This will write into src/main/resources/classement or src/main/resources/classement when possible,
                // otherwise into the runtime "classement/" directory.
                Classement.sauvegarderScore(trimmed, "Aleatoire", "Classique", 0, 0, "classement_libre.txt");
            } catch (Exception e) {
                e.printStackTrace();
            }

            FXCollections.sort(profiles);
            profileListView.getSelectionModel().select(trimmed);
                profileListView.scrollTo(trimmed); // <-- C'est cette ligne qui va forcer le défilement
            newProfileTextField.clear();

            addProfileBox.setVisible(false);
            addProfileBox.setManaged(false);
        }
    }

    // TODO: Add methods to load/save profiles and navigate to menu
}
