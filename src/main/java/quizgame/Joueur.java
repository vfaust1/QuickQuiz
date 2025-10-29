package quizgame;

public class Joueur {
    private String name;
    private int score;

    public Joueur(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void incrementerScore() {
        this.score++;
    }

    public void resetScore() {
        this.score = 0;
    }
}
