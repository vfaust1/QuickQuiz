package quizgame.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
// removed unused imports

public class ProfilMenuController {

    @FXML
    private Button btnChoisirProfil;

    @FXML
    private Button btnCreerProfil;

    @FXML
    private Button btnQuitter;

    @FXML
    private ImageView imgLogo;

    @FXML
    private void initialize() {
        // Ensure the button is wired even if FXML onAction fails for any reason
        if (btnChoisirProfil != null) {
            btnChoisirProfil.setOnAction(evt -> onChoisirProfilClick());
        }

        btnChoisirProfil.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                if (imgLogo != null) {
                    imgLogo.fitWidthProperty().bind(
                    Bindings.min(700,
                        Bindings.max(500,
                            newScene.widthProperty().multiply(0.55)
                        )
                    )
                    );
                }
            }
        });
    }

    @FXML
    private void onChoisirProfilClick() {
        naviguerVersMenu();
    }

    @FXML
    private void onQuitterClick() {
        Stage stage = (Stage) btnQuitter.getScene().getWindow();
        stage.close();
    }

    /**
     * Charge et affiche la scène du menu principal.
     */
    private void naviguerVersMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ProfileSelection.fxml"));

            // Récupérer le stage et son état courant avant de changer de scène
            Stage stage = (Stage) btnChoisirProfil.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();
            double oldW = stage.getWidth();
            double oldH = stage.getHeight();
            double oldX = stage.getX();
            double oldY = stage.getY();

            Parent root = fxmlLoader.load();
            // Créer la scène avec la même taille que l'ancienne pour éviter le rétrécissement
            Scene scene = new Scene(root, oldW, oldH);
            var cssUrl = getClass().getResource("/css/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            stage.setScene(scene);
            // Pass stage to selection controller
            try {
                ProfileSelectionController controller = fxmlLoader.getController();
                if (controller != null) {
                    controller.setStage(stage);
                }
            } catch (Exception ignored) {}
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
