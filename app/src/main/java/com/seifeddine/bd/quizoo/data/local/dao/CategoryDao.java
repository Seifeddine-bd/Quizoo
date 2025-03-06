package com.seifeddine.bd.quizoo.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.seifeddine.bd.quizoo.data.local.entity.Category;
import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM categories")
    List<Category> getAllCategoriesSync();

    @Query("SELECT COUNT(*) FROM categories")
    int getCategoryCount();

    @Query("DELETE FROM quizzes")
    void deleteAll();
    @Insert
    void insertAll(List<Category> categories);
}