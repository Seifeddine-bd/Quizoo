package com.seifeddine.bd.quizoo.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity(tableName = "quizzes")
public class Quiz {
    @PrimaryKey
    private int id;
    private int categoryId;
    private String question;
    private List<String> options;
    private int correctAnswerIndex;

    @Ignore
    public Quiz(int id, int categoryId, String question) {
        this.id = id;
        this.categoryId = categoryId;
        this.question = question;
    }

    public Quiz(int id, int categoryId, String question, List<String> options, int correctAnswerIndex) {
        this.id = id;
        this.categoryId = categoryId;
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    // Getters and setters
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