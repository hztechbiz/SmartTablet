package com.smart.tablet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smart.tablet.entities.Analytics;

@Dao
public interface AnalyticsDao {
    @Query("SELECT * FROM analytics")
    Analytics[] getAll();

    @Query("SELECT * FROM analytics WHERE id = :id LIMIT 1")
    Analytics get(int id);

    @Update
    void updateAll(Analytics... analytics);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Analytics... analytics);

    @Query("DELETE FROM analytics")
    void deleteAll();

    @Delete
    void delete(Analytics analytics);
}
