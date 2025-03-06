package com.seifeddine.bd.quizoo.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.seifeddine.bd.quizoo.data.local.OptionsConverter;

import java.util.List;

@Entity(tableName = "quizzes")
public class Quiz {
    @PrimaryKey
    @NonNull
    private String id;
    private int categoryId;
    private String question;
    @TypeConverters(OptionsConverter.class)
    private List<String> options;
    private int correctAnswerIndex;

    public Quiz(@NonNull String id, int categoryId, String question, List<String> options, int correctAnswerIndex) {
        this.id = id;
        this.categoryId = categoryId;
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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