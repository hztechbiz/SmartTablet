package com.smartapp.hztech.smarttebletapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smartapp.hztech.smarttebletapp.entities.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories")
    List<Category> getAll();

    @Query("SELECT * FROM categories WHERE id IN (:ids)")
    List<Category> getAll(int[] ids);

    @Query("SELECT * FROM categories WHERE id = :parent_id")
    List<Category> getAll(int parent_id);

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    Category get(int id);

    @Update
    void updateAll(Category... categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Category... categories);

    @Delete
    void delete(Category category);
}
