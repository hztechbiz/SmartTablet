package com.smartapp.hztech.smarttebletapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smartapp.hztech.smarttebletapp.entities.Sale;

@Dao
public interface SaleDao {
    @Query("SELECT * FROM sales")
    Sale[] getAll();

    @Query("SELECT * FROM sales WHERE id = :id LIMIT 1")
    Sale get(int id);

    @Query("SELECT * FROM sales WHERE service_id = :service_id")
    Sale[] getAll(int service_id);

    @Update
    void updateAll(Sale... sales);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Sale... sales);

    @Delete
    void delete(Sale sale);
}
