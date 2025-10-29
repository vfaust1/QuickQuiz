package quizgame;

public final class ProfilSession {
    private static String nomJoueur;

    private ProfilSession() {}

    public static String getNomJoueur() {
        return nomJoueur;
    }

    public static void setNomJoueur(String nom) {
        nomJoueur = nom;
    }

    public static boolean hasNom() {
        return nomJoueur != null && !nomJoueur.isBlank();
    }
}
