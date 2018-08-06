package com.smartapp.hztech.smarttebletapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smartapp.hztech.smarttebletapp.entities.Arrival;
import com.smartapp.hztech.smarttebletapp.entities.Offer;

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
