package com.smart.tablet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smart.tablet.entities.Sale;

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
