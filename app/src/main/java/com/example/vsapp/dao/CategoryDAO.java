package com.example.vsapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vsapp.entities.Category;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM categories ORDER BY categoryName ASC")
    List<Category> getAllCategories();
}
