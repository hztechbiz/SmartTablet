package com.smart.tablet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smart.tablet.entities.Testimonial;

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
