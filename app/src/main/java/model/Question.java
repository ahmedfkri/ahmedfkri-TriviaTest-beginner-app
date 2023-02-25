package model;

public class Question {

    private String answer;
    private boolean answerTure;

    public Question(String answer, boolean answerTure) {
        this.answer = answer;
        this.answerTure = answerTure;
    }

    public Question() {

    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswerTure() {
        return answerTure;
    }

    public void setAnswerTure(boolean answerTure) {
        this.answerTure = answerTure;
    }

    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", answerTure=" + answerTure +
                '}';
    }
}
