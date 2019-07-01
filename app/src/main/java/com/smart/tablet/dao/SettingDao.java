package com.smart.tablet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smart.tablet.entities.Setting;

@Dao
public interface SettingDao {
    @Query("SELECT value FROM setting WHERE name = :name LIMIT 1")
    String get(String name);

    @Query("SELECT * FROM setting WHERE name IN (:names)")
    Setting[] getAll(String[] names);

    @Update
    void updateAll(Setting... settings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Setting... settings);

    @Delete
    void delete(Setting setting);
}
