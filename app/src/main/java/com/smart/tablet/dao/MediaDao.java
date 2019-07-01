package com.smart.tablet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smart.tablet.entities.Media;

@Dao
public interface MediaDao {
    @Query("SELECT * FROM objects")
    Media[] getAll();

    @Query("SELECT * FROM objects WHERE id IN (:ids)")
    Media[] getAll(int... ids);

    @Query("SELECT * FROM objects WHERE id = :id LIMIT 1")
    Media get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Media... media);

    @Delete
    void delete(Media media);

    @Query("DELETE FROM objects")
    void deleteAll();
}
