package com.company;

import java.io.Serializable;

public class Question implements Serializable {
    private String question;
    private boolean answear;

    public Question(String question, boolean answer){
        this.question = question;
        this.answear = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswear(boolean answear) {
        this.answear = answear;
    }

    public boolean getAnswear() {
        return answear;
    }
}