package com.smart.tablet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smart.tablet.entities.Offer;

@Dao
public interface OfferDao {
    @Query("SELECT * FROM offers")
    Offer[] getAll();

    @Query("SELECT * FROM offers WHERE id = :id LIMIT 1")
    Offer get(int id);

    @Query("SELECT * FROM offers WHERE service_id = :service_id")
    Offer[] getAll(int service_id);

    @Update
    void updateAll(Offer... offers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Offer... offers);

    @Delete
    void delete(Offer offer);
}
