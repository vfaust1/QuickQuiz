package quizgame;

//import java.util.Arrays;
//import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.ArrayList;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String theme;

        System.out.println("\n\n\n\n\n\n\n\n");
        AffichageTxt.afficherTxt("txt/menu.txt");

        // Vérification du choix du menu
        int choixMenu = getChoixMenu(scanner);
        switch (choixMenu) {
            case 1:
                break;
            case 2:
                Classement.afficherTop("txt/classement.txt");
                scanner.nextLine();
                main(args);
                break;
            case 3:
                AffichageTxt.afficherTxt("txt/auRevoir.txt");
                scanner.close();
                System.exit(0);
                break;
        }

        // Console avant de jouer
        System.out.println("\n\n\n\n\n\n\n                              Quel est votre nom ?\n\n");
        String nomJoueur = getNomJoueur(scanner);
        Joueur joueur = new Joueur(nomJoueur);
        System.out.println("\n\n\n\n\n\n\n                              Bienvenue " + joueur.getName() + "\n");

        AffichageTxt.afficherTxt("txt/theme.txt");
        
        theme = getThemeValide(scanner);

        System.out.println("\n\n\n\n\n\n\n                              Combien de questions voulez vous ? (1-50)\n\n");

        int nbQuestions = getNbQuestions(scanner);

        // Sélection du mode de jeu
        System.out.println("\n\n\n\n\n\n\n                              Choisissez votre mode de jeu :\n");
        System.out.println("                              1. Mode Classique");
        System.out.println("                              2. Mode Chrono (1 minute + temps bonus/malus)\n");

        int mode = getModeValide(scanner);
        boolean modeChrono = (mode == 2);

        lancerPartie(theme, nbQuestions, scanner, joueur, modeChrono);

        // Fin de partie
        System.out.println("\n\n Felicitations " + joueur.getName() + " vous avez obtenu une note de " + joueur.getScore() + "/" + nbQuestions + " sur le theme : " + theme);
        String modeJeu = modeChrono ? "Chrono" : "Classique";
        Classement.sauvegarderScore(joueur.getName(), theme, modeJeu, joueur.getScore(), nbQuestions, "txt/classement.txt");

        System.out.println("\n\n Voulez vous rejouer ? [oui/non] \n");
        String fin = getChoixRejouer(scanner);
        
        if (fin.equals("oui")){
            main(args);
        } else {
            AffichageTxt.afficherTxt("txt/auRevoir.txt");
        }

        scanner.close();
        System.exit(0);
        
    }

    public static void lancerPartie(String theme, int nbQuestions, Scanner scanner, Joueur joueur, boolean modeChrono) {
        try {
            ArrayList<Question> questionnaire;
            
            if (theme.equals("Aleatoire")) {
                // Charger toutes les questions de tous les thèmes
                System.out.println("\nVous avez choisi des themes aleatoires");
                questionnaire = QuestionCsvLoader.chargerToutesQuestions();
            } else {
                // Charger les questions du thème spécifique
                System.out.println("\nVous avez choisi le theme " + theme);
                questionnaire = QuestionCsvLoader.chargerDepuisCsv("csv/" + theme.toString().toLowerCase() + ".csv");
                Collections.shuffle(questionnaire);
            }
            
            if (modeChrono) {
                lancerPartieChrono(questionnaire, scanner, joueur);
            } else {
                lancerPartieClassique(questionnaire, nbQuestions, scanner, joueur);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du fichier CSV : " + e.getMessage());
        }
    }
    
    private static void lancerPartieClassique(ArrayList<Question> questionnaire, int nbQuestions, Scanner scanner, Joueur joueur) {
        for (int i = 0; i < nbQuestions; i++) {
            System.out.println("\n\n" + (i+1) + ". " + questionnaire.get(i).toString());
            int reponse = getReponseValide(scanner, 1, 4);
            if (reponse - 1 == questionnaire.get(i).getGoodAnswer()){
                joueur.incrementerScore();
            }
        }
    }
    
    private static void lancerPartieChrono(ArrayList<Question> questionnaire, Scanner scanner, Joueur joueur) {
        System.out.println("\n\n=== MODE CHRONO ===");
        System.out.println("Vous avez 1 minute pour répondre au maximum de questions !");
        System.out.println("+2 secondes pour chaque bonne réponse");
        System.out.println("-2 secondes pour chaque mauvaise réponse");
        System.out.println("Appuyez sur Entrée pour commencer...");
        scanner.nextLine();
        
        long tempsDebut = System.currentTimeMillis();
        long tempsRestant = 60000; // 1 minute en millisecondes
        int questionIndex = 0;
        int questionsRepondues = 0;
        
        while (tempsRestant > 0 && questionIndex < questionnaire.size()) {
            long tempsActuel = System.currentTimeMillis();
            tempsRestant = 60000 - (tempsActuel - tempsDebut);
            
            if (tempsRestant <= 0) break;
            
            Question question = questionnaire.get(questionIndex);
            System.out.println("\n\n=== TEMPS RESTANT: " + (tempsRestant / 1000) + " secondes ===");
            System.out.println("Question " + (questionIndex + 1) + ": " + question.toString());
            
            // Lire la réponse avec un timeout
            int reponse = getReponseValideChrono(scanner, 1, 4, tempsRestant);
            
            if (reponse == -1) {
                System.out.println("Temps écoulé !");
                break;
            }
            
            questionsRepondues++;
            
            if (reponse - 1 == question.getGoodAnswer()) {
                joueur.incrementerScore();
                tempsDebut += 2000; // +2 secondes
                System.out.println("Correct ! +2 secondes");
            } else {
                tempsDebut -= 2000; // -2 secondes
                System.out.println("Incorrect ! -2 secondes");
            }
            
            questionIndex++;
        }
        
        System.out.println("\n\n=== FIN DU MODE CHRONO ===");
        System.out.println("Questions répondues: " + questionsRepondues);
        System.out.println("Bonnes réponses: " + joueur.getScore());
    }
    
    // Méthodes de validation des entrées utilisateur
    
    private static int getChoixMenu(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choix = Integer.parseInt(input);
                if (choix >= 1 && choix <= 3) {
                    return choix;
                } else {
                    System.out.println("ERREUR : Veuillez entrer 1, 2 ou 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERREUR : Veuillez entrer un nombre valide (1, 2 ou 3).");
            }
        }
    }
    
    private static String getNomJoueur(Scanner scanner) {
        while (true) {
            String nom = scanner.nextLine().trim();
            if (!nom.isEmpty()) {
                return nom;
            } else {
                System.out.println("ERREUR : Le nomne doit pas etre vide. Veuillez réessayer :");
            }
        }
    }
    
    private static String getThemeValide(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choix = Integer.parseInt(input);
                if (choix >= 1 && choix <= 6) {
                    if (choix == 6) {
                        return "Aleatoire";
                    }
                    return Theme.equivalent(input);
                } else {
                    System.out.println("ERREUR : Veuillez entrer un nombre entre 1 et 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERREUR : Veuillez entrer un nombre valide (1-6).");
            }
        }
    }
    
    private static int getNbQuestions(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int nb = Integer.parseInt(input);
                if (nb >= 1 && nb <= 50) {
                    return nb;
                } else {
                    System.out.println("ERREUR : Le nombre de questions doit être entre 1 et 50. Veuillez réessayer :");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERREUR : Veuillez entrer un nombre valide (1-50).");
            }
        }
    }
    
    private static int getReponseValide(Scanner scanner, int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int reponse = Integer.parseInt(input);
                if (reponse >= min && reponse <= max) {
                    return reponse;
                } else {
                    System.out.println("ERREUR : Veuillez entrer un nombre entre " + min + " et " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERREUR : Veuillez entrer un nombre valide (" + min + "-" + max + ").");
            }
        }
    }
    
    private static String getChoixRejouer(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("oui") || input.equals("o") || input.equals("yes") || input.equals("y")) {
                return "oui";
            } else {
                return "non";
            }
        }
    }
    
    private static int getModeValide(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int mode = Integer.parseInt(input);
                if (mode >= 1 && mode <= 2) {
                    return mode;
                } else {
                    System.out.println("ERREUR : Veuillez entrer 1 ou 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERREUR : Veuillez entrer un nombre valide (1 ou 2).");
            }
        }
    }
    
    private static int getReponseValideChrono(Scanner scanner, int min, int max, long tempsRestant) {
        long debutLecture = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - debutLecture < tempsRestant) {
            if (scanner.hasNextLine()) {
                try {
                    String input = scanner.nextLine().trim();
                    int reponse = Integer.parseInt(input);
                    if (reponse >= min && reponse <= max) {
                        return reponse;
                    } else {
                        System.out.println("ERREUR : Veuillez entrer un nombre entre " + min + " et " + max + ".");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ERREUR : Veuillez entrer un nombre valide (" + min + "-" + max + ").");
                }
            }
        }
        
        return -1; // Timeout
    }
    
}
