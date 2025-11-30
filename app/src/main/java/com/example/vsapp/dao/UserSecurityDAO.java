package com.example.vsapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.vsapp.entities.UserSecurity;

@Dao
public interface UserSecurityDAO {

    @Query("SELECT * FROM user_security LIMIT 1")
    UserSecurity getUserSecurity();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserSecurity security);
}
