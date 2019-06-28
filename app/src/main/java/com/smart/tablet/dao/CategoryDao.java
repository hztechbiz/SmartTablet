package com.smart.tablet.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.Update;

import com.smart.tablet.entities.Category;

@Dao
public interface CategoryDao {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT c.id, c.name, c.description, c.parent_id, c.is_marketing_partner, c.embed_url, c.meta, c.display_order, (SELECT count(*) FROM categories WHERE parent_id = c.id) AS children_count FROM categories AS c left join categories AS c1 ON c1.id = c.parent_id WHERE c.parent_id = 0 ORDER BY c.display_order")
    Category[] getAll();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT c.id, c.name, c.description, c.parent_id, c.is_marketing_partner, c.embed_url, c.meta, c.display_order, (SELECT count(*) FROM categories WHERE parent_id = c.id) AS children_count FROM categories AS c left join categories AS c1 ON c1.id = c.parent_id WHERE c.id IN (:ids) ORDER BY c.display_order")
    Category[] getAll(int[] ids);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT c.id, c.name, c.description, c.parent_id, c.is_marketing_partner, c.embed_url, c.meta, c.display_order, (SELECT count(*) FROM categories WHERE parent_id = c.id) AS children_count FROM categories AS c left join categories AS c1 ON c1.id = c.parent_id WHERE c.parent_id = :parent_id ORDER BY c.display_order")
    Category[] getAll(int parent_id);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT c.id, c.name, c.description, c.parent_id, c.is_marketing_partner, c.embed_url, c.meta, c.display_order, (SELECT count(*) FROM categories WHERE parent_id = c.id) AS children_count FROM categories AS c left join categories AS c1 ON c1.id = c.parent_id WHERE c.is_marketing_partner = 0 AND c.parent_id = 0 ORDER BY c.display_order")
    Category[] getGsdAll();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT c.id, c.name, c.description, c.parent_id, c.is_marketing_partner, c.embed_url, c.meta, c.display_order, (SELECT count(*) FROM categories WHERE parent_id = c.id) AS children_count FROM categories AS c left join categories AS c1 ON c1.id = c.parent_id WHERE c.is_marketing_partner = 1 AND c.parent_id = 0 ORDER BY c.display_order")
    Category[] getMpAll();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT c.id, c.name, c.description, c.parent_id, c.is_marketing_partner, c.embed_url, c.meta, c.display_order, (SELECT count(*) FROM categories WHERE parent_id = c.id) AS children_count FROM categories AS c left join categories AS c1 ON c1.id = c.parent_id WHERE c.id = :id  ORDER BY c.display_order LIMIT 1")
    Category get(int id);

    @Update
    void updateAll(Category... categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Category... categories);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM categories")
    void deleteAll();

    @Query("DELETE FROM categories WHERE id IN (:ids)")
    void deleteAll(int... ids);
}
