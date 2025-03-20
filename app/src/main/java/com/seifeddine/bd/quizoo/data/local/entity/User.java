package com.seifeddine.bd.quizoo.data.local.entity;

import java.util.Date;

public class User {
    private String userId;
    private String email;
    private String username;
    private Date createdAt;
    private int quizzesTaken;

    // No-args constructor needed for Firestore
    public User() {}

    public User(String userId, String email, String username, Date createdAt, int quizzesTaken) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.createdAt = createdAt;
        this.quizzesTaken = quizzesTaken;
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public int getQuizzesTaken() { return quizzesTaken; }
    public void setQuizzesTaken(int quizzesTaken) { this.quizzesTaken = quizzesTaken; }

    public void incrementQuizzesTaken() { this.quizzesTaken++; }
}