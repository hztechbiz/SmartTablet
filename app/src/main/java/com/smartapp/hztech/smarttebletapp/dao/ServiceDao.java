package com.smartapp.hztech.smarttebletapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smartapp.hztech.smarttebletapp.entities.Service;

import java.util.List;

@Dao
public interface ServiceDao {
    @Query("SELECT * FROM services")
    List<Service> getAll();

    @Query("SELECT * FROM services WHERE id IN (:ids)")
    List<Service> getAll(int[] ids);

    @Query("SELECT * FROM services WHERE category_id = :category_id")
    List<Service> getAll(int category_id);

    @Query("SELECT * FROM services WHERE id = :ids LIMIT 1")
    Service get(int id);

    @Update
    void updateAll(Service... services);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Service... services);

    @Delete
    void delete(Service service);
}
