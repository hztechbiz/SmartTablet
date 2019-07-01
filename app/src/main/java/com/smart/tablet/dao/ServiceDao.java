package com.smart.tablet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("DELETE FROM services WHERE id IN (:ids)")
    void deleteAll(int... ids);

    @Query("DELETE FROM services")
    void deleteAll();
}
