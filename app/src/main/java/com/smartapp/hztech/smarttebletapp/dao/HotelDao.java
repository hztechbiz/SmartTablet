package com.smartapp.hztech.smarttebletapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smartapp.hztech.smarttebletapp.entities.Hotel;

@Dao
public interface HotelDao {
    @Query("SELECT * FROM hotels LIMIT 1")
    Hotel getFirst();

    @Query("SELECT * FROM hotels WHERE name = :name LIMIT 1")
    Hotel get(String name);

    @Query("SELECT * FROM hotels WHERE id = :id LIMIT 1")
    Hotel get(int id);

    @Update
    void updateAll(Hotel... hotels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Hotel... hotels);

    @Delete
    void delete(Hotel hotel);
}
