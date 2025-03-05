package com.seifeddine.bd.quizoo.data.remote.dto;

public class AnalyticsRequest {
    private int quizId;
    private String userAnswer;
    private long timeTaken;

    // Constructor, getters, and setters
    public AnalyticsRequest(int quizId, String userAnswer, long timeTaken) {
        this.quizId = quizId;
        this.userAnswer = userAnswer;
        this.timeTaken = timeTaken;
    }

    public int getQuizId() { return quizId; }
    public String getUserAnswer() { return userAnswer; }
    public long getTimeTaken() { return timeTaken; }
}