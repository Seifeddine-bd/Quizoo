package com.seifeddine.bd.quizoo.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.seifeddine.bd.quizoo.data.local.entity.Quiz;
import java.util.List;

@Dao
public interface QuizDao {
    @Insert
    void insertQuizzes(List<Quiz> quizzes);

    @Query("SELECT * FROM quizzes WHERE categoryId = :categoryId")
    LiveData<List<Quiz>> getQuizzesByCategory(int categoryId); // Changed to return LiveData
}