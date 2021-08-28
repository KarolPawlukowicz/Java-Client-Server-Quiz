package com.company;
import java.io.Serializable;

public class Question implements Serializable {
    private String question;
    private boolean answer;

    public Question(String question, boolean answer){
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(boolean answear) {
        this.answer = answear;
    }

    public boolean getAnswer() {
        return answer;
    }
}