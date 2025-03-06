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
    void insertAll(List<Quiz> quizzes);

    @Query("SELECT * FROM quizzes WHERE categoryId = :categoryId")
    LiveData<List<Quiz>> getQuizzesByCategory(int categoryId);

    @Query("SELECT * FROM quizzes WHERE id = :quizId")
    LiveData<Quiz> getQuizById(String quizId);

    @Query("SELECT * FROM quizzes")
    List<Quiz> getAllQuizzesSync();

    @Query("DELETE FROM quizzes")
    void deleteAll();
}