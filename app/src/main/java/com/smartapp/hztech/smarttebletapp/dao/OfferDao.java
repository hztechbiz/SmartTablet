package com.smartapp.hztech.smarttebletapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smartapp.hztech.smarttebletapp.entities.Offer;

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
