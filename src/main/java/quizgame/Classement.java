package quizgame;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Classement {
        
    public static void sauvegarderScore(String nom, String theme, String mode, int score, int nbQuestions, String cheminFichier) {
        String filename = Paths.get(cheminFichier).getFileName().toString();

        // First try: write into the project's resources folder so devs see changes in the repo.
        // Some contributors use French folder name `src/main/resources` — try that first,
        // then fall back to the English `src/main/resources`.
    // Try French-named resources directory first (some contributors used `resources`),
    // then the standard `resources` directory.
    Path devResourceDirFr = Paths.get("src", "main", "resources", "classement");
    Path devResourceDirEn = Paths.get("src", "main", "resources", "classement");
    Path[] candidates = new Path[] { devResourceDirFr, devResourceDirEn };
        for (Path devResourceDir : candidates) {
            Path devTarget = devResourceDir.resolve(filename);
            try {
                if (!Files.exists(devResourceDir)) {
                    Files.createDirectories(devResourceDir);
                }
                try (FileWriter fw = new FileWriter(devTarget.toFile(), true)) {
                    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    String nbQuestionsStr = mode.equals("Chrono") ? "-" : String.valueOf(nbQuestions);
                    fw.write(nom + ";" + theme + ";" + score + ";" + nbQuestionsStr + ";" + now + "\n");
                    return; // success, we're done
                }
            } catch (IOException ignored) {
                // try next candidate
            }
        }

        // Fallback: write to an external runtime-writable classement/ directory
        try {
            Path externalDir = Paths.get("classement");
            if (!Files.exists(externalDir)) {
                Files.createDirectories(externalDir);
            }
            Path target = externalDir.resolve(filename);
            try (FileWriter fw = new FileWriter(target.toFile(), true)) {
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String nbQuestionsStr = mode.equals("Chrono") ? "-" : String.valueOf(nbQuestions);
                fw.write(nom + ";" + theme + ";" + score + ";" + nbQuestionsStr + ";" + now + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erreur de sauvegarde du score : " + e.getMessage());
        }
    }

    public static List<String[]> chargerClassement(String cheminFichier) {
        List<String[]> scores = new ArrayList<>();
        String filename = Paths.get(cheminFichier).getFileName().toString();
        Path external = Paths.get("classement").resolve(filename);

        // If a developer resource exists at src/main/resources/classement/<filename>,
        // use it exclusively (the user requested to show only the dev resource file).
        Path devResourceFr = Paths.get("src", "main", "resources", "classement").resolve(filename);
        if (Files.exists(devResourceFr)) {
            try (BufferedReader br = Files.newBufferedReader(devResourceFr, StandardCharsets.UTF_8)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(";");
                    if (parts.length == 5 || parts.length == 6) scores.add(parts);
                }
                return scores;
            } catch (IOException e) {
                System.out.println("Impossible de charger le classement depuis " + devResourceFr + " : " + e.getMessage());
                // fall through to aggregated behavior below
            }
        }

        // Otherwise fall back to aggregated behavior (external runtime file + packaged resource)
        LinkedHashSet<String> rawLines = new LinkedHashSet<>();

        // 1) External runtime file (classement/filename) if present
        if (Files.exists(external)) {
            try (BufferedReader br = Files.newBufferedReader(external, StandardCharsets.UTF_8)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    rawLines.add(line.trim());
                }
            } catch (IOException e) {
                System.out.println("Impossible de charger le classement depuis " + external + " : " + e.getMessage());
            }
        }

        // 2) Packaged resource under /classement/<filename> on the classpath
        InputStream is = Classement.class.getClassLoader().getResourceAsStream("classement/" + filename);
        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    rawLines.add(line.trim());
                }
            } catch (IOException e) {
                System.out.println("Impossible de charger le classement depuis la resource : " + e.getMessage());
            }
        }

        // 3) Last fallback: try the provided cheminFichier path directly (keeps compatibility)
        if (rawLines.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    rawLines.add(line.trim());
                }
            } catch (IOException e) {
                System.out.println("Impossible de charger le classement depuis le chemin fallback : " + e.getMessage());
            }
        }

        // Parse collected raw lines into the scores list
        for (String raw : rawLines) {
            String[] parts = raw.split(";");
            if (parts.length == 5 || parts.length == 6) {
                scores.add(parts);
            }
        }

        return scores;
    }

    // Trie et affiche les 10 meilleurs
    public static void afficherTop(String cheminFichier) {
        List<String[]> scores = chargerClassement(cheminFichier);
        
        if (scores.isEmpty()) {
            System.out.println("\n--- Classement Meilleurs Scores ---\n\n");
            System.out.println("Aucun score enregistré pour le moment.\n");
            System.out.println("\nAppuyez sur 'Entrer' pour continuer...\n");
            return;
        }
        
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
                // Les deux en mode chrono : trier par score décroissant
                return Integer.compare(scoreB, scoreA);
            } else if (isChronoA) {
                return -1; // Chrono en premier
            } else if (isChronoB) {
                return 1; // Chrono en premier
            } else {
                // Mode libre : calculer le ratio pour chaque entrée (score/nbQuestions)
                double ratioA = (double) scoreA / Double.parseDouble(a[nbQuestionsIndexA]);
                double ratioB = (double) scoreB / Double.parseDouble(b[nbQuestionsIndexB]);
                
                // Trier d'abord par ratio décroissant (meilleur ratio en premier)
                int ratioComparison = Double.compare(ratioB, ratioA);
                if (ratioComparison != 0) {
                    return ratioComparison;
                }
                
                // En cas d'égalité de ratio, trier par nombre de questions décroissant
                int nbQuestionsA = Integer.parseInt(a[nbQuestionsIndexA]);
                int nbQuestionsB = Integer.parseInt(b[nbQuestionsIndexB]);
                return Integer.compare(nbQuestionsB, nbQuestionsA);
            }
        });
        System.out.println("\n--- Classement Meilleurs Scores ---\n\n");
        int limite = Math.min(10, scores.size());
        for (int i=0; i<limite; i++) {
            String[] entry = scores.get(i);
            // Déterminer les indices selon le format
            int nameIndex = 0;
            int themeIndex = 1;
            int scoreIndex = (entry.length == 6) ? 3 : 2;
            int nbQuestionsIndex = (entry.length == 6) ? 4 : 3;
            int dateIndex = (entry.length == 6) ? 5 : 4;
            String modeStr = (entry.length == 6) ? " (" + entry[2] + ")" : "";
            
            if (entry[nbQuestionsIndex].equals("-")) {
                // Mode chrono : afficher uniquement le score
                System.out.println((i+1) + ". " + entry[nameIndex] + " : " + entry[themeIndex] + modeStr + " = " + entry[scoreIndex] + " points - " + entry[dateIndex] + "\n");
            } else {
                // Mode libre : afficher score/nbQuestions et pourcentage
                int score = Integer.parseInt(entry[scoreIndex]);
                int nbQuestions = Integer.parseInt(entry[nbQuestionsIndex]);
                double ratio = (double) score / nbQuestions;
                double pourcentage = ratio * 100;
                System.out.println((i+1) + ". " + entry[nameIndex] + " : " + entry[themeIndex] + modeStr + " = " + entry[scoreIndex] + "/" + entry[nbQuestionsIndex] + " (" + String.format("%.1f", pourcentage) + "%) - " + entry[dateIndex] + "\n");
            }
        }
        System.out.println("\n\nAppuyez sur 'Entrer' pour continuer...\n");
    }
}
