// com/seifeddine/bd/quizoo/data/local/dao/QuizDao.java
package com.seifeddine.bd.quizoo.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.seifeddine.bd.quizoo.data.local.entity.Quiz;

import java.util.List;

@Dao
public interface QuizDao {
    @Query("SELECT * FROM quizzes WHERE categoryId = :categoryId")
    List<Quiz> getQuizzesByCategory(int categoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuizzes(List<Quiz> quizzes); // Use Quiz, not NetworkQuiz
}