// com/seifeddine/bd/quizoo/data/local/entity/Quiz.java
package com.seifeddine.bd.quizoo.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.seifeddine.bd.quizoo.data.local.OptionsConverter;

import java.util.List;

@Entity(tableName = "quizzes")
@TypeConverters({OptionsConverter.class})
public class Quiz {
    @PrimaryKey
    private int id;
    private int categoryId;
    private String question;
    private List<String> options;
    private int correctAnswerIndex;

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