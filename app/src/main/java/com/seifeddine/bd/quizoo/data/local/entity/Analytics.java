package com.seifeddine.bd.quizoo.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "analytics")
public class Analytics {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String quizId;
    private String selectedAnswer;
    private long timeTaken;

    // Constructor
    public Analytics(String quizId, String selectedAnswer, long timeTaken) {
        this.quizId = quizId;
        this.selectedAnswer = selectedAnswer;
        this.timeTaken = timeTaken;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }
}