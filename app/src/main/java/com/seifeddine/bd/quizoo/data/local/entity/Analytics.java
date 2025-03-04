package com.seifeddine.bd.quizoo.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "analytics")
public class Analytics {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int quizId;
    private int userAnswer;
    private long timeTaken;

    public Analytics(int quizId, int userAnswer, long timeTaken) {
        this.quizId = quizId;
        this.userAnswer = userAnswer;
        this.timeTaken = timeTaken;
    }

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }
    public int getUserAnswer() { return userAnswer; }
    public void setUserAnswer(int userAnswer) { this.userAnswer = userAnswer; }
    public long getTimeTaken() { return timeTaken; }
    public void setTimeTaken(long timeTaken) { this.timeTaken = timeTaken; }
}