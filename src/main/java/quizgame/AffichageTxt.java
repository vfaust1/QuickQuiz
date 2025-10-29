package quizgame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AffichageTxt {
    public static void afficherTxt(String cheminFichier) {
        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }
}
