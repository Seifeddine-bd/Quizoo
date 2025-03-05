package com.seifeddine.bd.quizoo.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.seifeddine.bd.quizoo.data.local.entity.Analytics;
import java.util.List;

@Dao
public interface AnalyticsDao {
    @Insert
    void insert(Analytics analytics);

    @Query("SELECT * FROM analytics")
    LiveData<List<Analytics>> getAllAnalytics();

    @Query("SELECT * FROM analytics")
    List<Analytics> getAllAnalyticsSync();
}