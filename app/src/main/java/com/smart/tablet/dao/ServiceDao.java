package com.smart.tablet.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smart.tablet.entities.Service;

import java.util.List;

@Dao
public interface ServiceDao {
    @Query("SELECT * FROM services")
    com.smart.tablet.entities.Service[] getAll();

    @Query("SELECT * FROM services WHERE id IN (:ids)")
    com.smart.tablet.entities.Service[] getAll(int[] ids);

    @Query("SELECT * FROM services WHERE category_id = :category_id")
    com.smart.tablet.entities.Service[] getAll(int category_id);

    @Query("SELECT * FROM services WHERE id = :id LIMIT 1")
    com.smart.tablet.entities.Service get(int id);

    @Update
    void updateAll(com.smart.tablet.entities.Service... services);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(com.smart.tablet.entities.Service... services);

    @Delete
    void delete(com.smart.tablet.entities.Service service);

    @Query("DELETE FROM services")
    void deleteAll();
}
