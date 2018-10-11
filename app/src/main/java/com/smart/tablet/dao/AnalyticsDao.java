package com.smart.tablet.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smart.tablet.entities.Analytics;
import com.smart.tablet.entities.Testimonial;

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

    @Delete
    void delete(Analytics analytics);
}
