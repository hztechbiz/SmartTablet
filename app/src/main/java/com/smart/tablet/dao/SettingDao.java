package com.smart.tablet.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
