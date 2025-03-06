package com.seifeddine.bd.quizoo.data.remote.dto;

public class AnalyticsRequest {
    private String quizId;
    private String selectedAnswer;
    private long timeTaken;

    public AnalyticsRequest(String quizId, String selectedAnswer, long timeTaken) {
        this.quizId = quizId;
        this.selectedAnswer = selectedAnswer;
        this.timeTaken = timeTaken;
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