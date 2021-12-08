package com.springMVC.lab3.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Results {
    @Id
    private int quizId;
    private int score;

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
