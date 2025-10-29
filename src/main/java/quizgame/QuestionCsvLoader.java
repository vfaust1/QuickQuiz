package quizgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;

public class QuestionCsvLoader {

    public static ArrayList<Question> chargerDepuisCsv(String cheminCsv) throws IOException {
        ArrayList<Question> questions = new ArrayList<>();
        try (BufferedReader br = openBufferedReader(cheminCsv)) {
            String line;
            while ((line = br.readLine()) != null) {
                // Ignore lignes vides
                if (line.trim().isEmpty()) continue;

                // Split basique par point-virgule ; ajuster selon votre séparateur
                String[] parts = line.split(";", -1); // -1 pour conserver champs vides
                if (parts.length < 6) {
                    // Ligne incomplète: on skip
                    continue;
                }
                // Skip header line if present
                if (parts[0].trim().equalsIgnoreCase("question") || parts[0].trim().equalsIgnoreCase("thème") ) continue;

                // Normalisation simple des apostrophes/guillemets et espaces mal placés
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i] == null) continue;
                    String s = parts[i].trim();
                    s = s.replace('\u2019', '\'') // right single quotation mark ’ -> '
                         .replace('\u2018','\'') // left single quotation mark ‘ -> '
                         .replace('\u201C','\"') // left double “ -> "
                         .replace('\u201D','\"'); // right double ” -> "
                    // retirer espace avant apostrophe : "l 'origine" -> "l'origine"
                    s = s.replaceAll("\\s+'", "'");
                    parts[i] = s;
                }

                String questionTexte = parts[0].trim();
                String c1 = parts[1].trim();
                String c2 = parts[2].trim();
                String c3 = parts[3].trim();
                String c4 = parts[4].trim();

                int goodIndex1Based = -1;
                String last = parts[5].trim();
                // Essayer de parser directement
                try {
                    goodIndex1Based = Integer.parseInt(last);
                } catch (NumberFormatException e) {
                    // tenter d'extraire un nombre depuis la chaîne (ex: "4xyz" ou "... 4")
                    Pattern p = Pattern.compile("(\\d+)");
                    Matcher m = p.matcher(last);
                    if (m.find()) {
                        try {
                            goodIndex1Based = Integer.parseInt(m.group(1));
                        } catch (NumberFormatException ex) {
                            // ignore, restera -1
                        }
                    }
                }
                if (goodIndex1Based < 1 || goodIndex1Based > 4) {
                    // index hors bornes ou non trouvé : on skip la ligne
                    continue;
                }
                int goodIndex0Based = goodIndex1Based - 1;

                String[] choices = new String[]{c1, c2, c3, c4};
                Question q = new Question(questionTexte, choices, goodIndex0Based);
                // Mélanger les choix de réponse
                q = melangerChoix(q);
                questions.add(q);
            }
        }
        return questions;
    }

    /**
     * Ouvre un BufferedReader pour un fichier CSV en essayant d'abord
     * de le charger depuis le classpath (src/main/resources), puis
     * en retombant sur le système de fichiers si la ressource n'existe pas.
     *
     * @param cheminCsv chemin relatif (ex: "csv/cinema.csv" ou un chemin absolu)
     * @return BufferedReader prêt à lire en UTF-8
     * @throws IOException si le fallback fichier échoue
     */
    private static BufferedReader openBufferedReader(String cheminCsv) throws IOException {
        // Essayer la ressource dans le classpath (packaged inside JAR under /csv/...)
        InputStream is = QuestionCsvLoader.class.getClassLoader().getResourceAsStream(cheminCsv);
        if (is != null) {
            return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        }

        // Fallback: essayer en tant que chemin sur le système de fichiers
        return Files.newBufferedReader(Paths.get(cheminCsv), StandardCharsets.UTF_8);
    }
    
    /**
     * Mélange l'ordre des réponses d'une question
     * @param q Question avec réponses dans l'ordre original
     * @return Nouvelle question avec réponses mélangées
     */
    public static Question melangerChoix(Question q) {
        String[] src = q.getChoices();
        ArrayList<Integer> idx = new ArrayList<>(src.length);
        for (int i = 0; i < src.length; i++) idx.add(i);
        Collections.shuffle(idx);
        String[] shuffled = new String[src.length];
        int newGood = -1;
        for (int i = 0; i < idx.size(); i++) {
            shuffled[i] = src[idx.get(i)];
            if (idx.get(i) == q.getGoodAnswer()) newGood = i;
        }
        return new Question(q.getQuestion(), shuffled, newGood);
    }

    /**
     * Charge toutes les questions de tous les thèmes disponibles
     * @return ArrayList contenant toutes les questions mélangées
     */
    public static ArrayList<Question> chargerToutesQuestions() {
        ArrayList<Question> toutesQuestions = new ArrayList<>();
        String[] themes = {"cinema", "football", "jeuxvideo", "manga", "musique", "bandedessinee", "animation", "seriestv"};
        
        for (String theme : themes) {
            try {
                ArrayList<Question> questionsTheme = chargerDepuisCsv("csv/" + theme + ".csv");
                toutesQuestions.addAll(questionsTheme);
            } catch (IOException e) {
                System.out.println("Erreur lors du chargement du thème " + theme + " : " + e.getMessage());
            }
        }
        
        // Mélanger toutes les questions
        java.util.Collections.shuffle(toutesQuestions);
        return toutesQuestions;
    }

}
