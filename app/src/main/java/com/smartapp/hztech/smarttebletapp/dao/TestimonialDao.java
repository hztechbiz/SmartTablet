package com.smartapp.hztech.smarttebletapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smartapp.hztech.smarttebletapp.entities.Testimonial;

@Dao
public interface TestimonialDao {
    @Query("SELECT * FROM testimonials")
    Testimonial[] getAll();

    @Query("SELECT * FROM testimonials WHERE id = :id LIMIT 1")
    Testimonial get(int id);

    @Query("SELECT * FROM testimonials WHERE service_id = :service_id")
    Testimonial[] getAll(int service_id);

    @Update
    void updateAll(Testimonial... testimonials);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Testimonial... testimonials);

    @Delete
    void delete(Testimonial testimonial);
}
