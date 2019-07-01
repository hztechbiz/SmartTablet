package com.smart.tablet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smart.tablet.entities.Arrival;

@Dao
public interface ArrivalDao {
    @Query("SELECT * FROM arrivals")
    Arrival[] getAll();

    @Query("SELECT * FROM arrivals WHERE id = :id LIMIT 1")
    Arrival get(int id);

    @Query("SELECT * FROM arrivals WHERE service_id = :service_id")
    Arrival[] getAll(int service_id);

    @Update
    void updateAll(Arrival... arrivals);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Arrival... arrivals);

    @Delete
    void delete(Arrival arrival);
}
