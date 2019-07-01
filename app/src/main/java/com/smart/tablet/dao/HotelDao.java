package com.smart.tablet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
