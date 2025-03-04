package com.seifeddine.bd.quizoo.data.remote.dto;

// AnalyticsRequest.java
public class AnalyticsRequest {
    private int quizId;
    private int userAnswer;
    private long timeTaken;

    public AnalyticsRequest(int quizId, int userAnswer, long timeTaken) {
        this.quizId = quizId;
        this.userAnswer = userAnswer;
        this.timeTaken = timeTaken;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(int userAnswer) {
        this.userAnswer = userAnswer;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

}