package com.seifeddine.bd.quizoo.data.remote.dto;

public class NetworkCategory {
    private int id;
    private String name;
    private int categoryImage;
    public NetworkCategory(int id, String name, int categoryImage) {
        this.id = id;
        this.name = name;
        this.categoryImage = categoryImage;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}