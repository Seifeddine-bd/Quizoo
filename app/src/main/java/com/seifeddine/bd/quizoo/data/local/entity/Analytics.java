package com.seifeddine.bd.quizoo.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "analytics")
public class Analytics {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int quizId;
    private String userAnswer;
    private long timeTaken;

    public Analytics(int quizId, String userAnswer, long timeTaken) {
        this.quizId = quizId;
        this.userAnswer = userAnswer;
        this.timeTaken = timeTaken;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    public long getTimeTaken() { return timeTaken; }
    public void setTimeTaken(long timeTaken) { this.timeTaken = timeTaken; }
}