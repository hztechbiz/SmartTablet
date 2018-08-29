package com.smart.tablet.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smart.tablet.entities.Hotel;

@Dao
public interface HotelDao {
    @Query("SELECT * FROM hotels LIMIT 1")
    com.smart.tablet.entities.Hotel getFirst();

    @Query("SELECT * FROM hotels WHERE name = :name LIMIT 1")
    com.smart.tablet.entities.Hotel get(String name);

    @Query("SELECT * FROM hotels WHERE id = :id LIMIT 1")
    com.smart.tablet.entities.Hotel get(int id);

    @Update
    void updateAll(com.smart.tablet.entities.Hotel... hotels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(com.smart.tablet.entities.Hotel... hotels);

    @Delete
    void delete(com.smart.tablet.entities.Hotel hotel);
}
