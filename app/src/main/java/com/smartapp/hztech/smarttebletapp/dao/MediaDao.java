package com.smartapp.hztech.smarttebletapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.smartapp.hztech.smarttebletapp.entities.Media;

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
