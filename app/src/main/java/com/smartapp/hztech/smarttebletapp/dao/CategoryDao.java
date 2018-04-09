package com.smartapp.hztech.smarttebletapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smartapp.hztech.smarttebletapp.entities.Category;

@Dao
public interface CategoryDao {
    @Query("SELECT c.id, c.name, c.description, c.parent_id, (SELECT count(*) FROM categories WHERE parent_id = c.id) AS children_count FROM categories AS c left join categories AS c1 ON c1.id = c.parent_id")
    Category[] getAll();

    @Query("SELECT c.id, c.name, c.description, c.parent_id, (SELECT count(*) FROM categories WHERE parent_id = c.id) AS children_count FROM categories AS c left join categories AS c1 ON c1.id = c.parent_id WHERE c.id IN (:ids)")
    Category[] getAll(int[] ids);

    @Query("SELECT c.id, c.name, c.description, c.parent_id, (SELECT count(*) FROM categories WHERE parent_id = c.id) AS children_count FROM categories AS c left join categories AS c1 ON c1.id = c.parent_id WHERE c.parent_id = :parent_id")
    Category[] getAll(int parent_id);

    @Query("SELECT c.id, c.name, c.description, c.parent_id, (SELECT count(*) FROM categories WHERE parent_id = c.id) AS children_count FROM categories AS c left join categories AS c1 ON c1.id = c.parent_id WHERE c.id = :id LIMIT 1")
    Category get(int id);

    @Update
    void updateAll(Category... categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Category... categories);

    @Delete
    void delete(Category category);
}
