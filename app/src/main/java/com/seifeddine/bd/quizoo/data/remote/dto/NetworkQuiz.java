package com.seifeddine.bd.quizoo.data.remote.dto;

import java.util.List;

public class NetworkQuiz {
    private String id;
    private int categoryId;
    private String question;
    private List<String> options;
    private int correctAnswerIndex;

    public NetworkQuiz(String id, int categoryId, String question, List<String> options, int correctAnswerIndex) {
        this.id = id;
        this.categoryId = categoryId;
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
}