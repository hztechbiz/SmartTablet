package com.smart.tablet.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smart.tablet.entities.Device;

@Dao
public interface DeviceDao {
    @Query("SELECT * FROM devices")
    Device[] getAll();

    @Query("SELECT * FROM devices WHERE id = :id LIMIT 1")
    Device get(int id);

    @Update
    void updateAll(Device... devices);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Device... devices);

    @Delete
    void delete(Device device);
}
