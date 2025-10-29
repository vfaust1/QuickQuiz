package quizgame;

public class Question {
    private String question;
    private String[] choices;
    private int goodAnswer;

    public Question(String question, String[] choices, int goodAnswer) {
        this.question = question;
        this.choices = choices;
        this.goodAnswer = goodAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getChoices() {
        return choices;
    }

    public int getGoodAnswer() {
        return goodAnswer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(question+"\n\n");
        for (int i = 0; i < choices.length; i++) {
            sb.append((i + 1)).append(". ").append(choices[i]).append("\n");
        }
        return sb.toString();
    }
}
