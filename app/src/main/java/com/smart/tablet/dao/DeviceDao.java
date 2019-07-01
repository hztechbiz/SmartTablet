package com.smart.tablet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
