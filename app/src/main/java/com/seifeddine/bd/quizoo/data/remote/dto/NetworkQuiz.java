// com/seifeddine/bd/quizoo/data/remote/dto/NetworkQuiz.java
package com.seifeddine.bd.quizoo.data.remote.dto;

import java.util.List;

public class NetworkQuiz {
    private int id;
    private int categoryId;
    private String question;
    private List<String> options;
    private int correctAnswerIndex;

    // Getters, setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public void setCorrectAnswerIndex(int correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }
}