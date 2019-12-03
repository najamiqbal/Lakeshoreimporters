package com.dleague.lakeshoreimporters.dtos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDTOInterface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserDTO... userDTO);

    @Update
    void update(UserDTO userDTO);

    @Query("Select * FROM UserDTO")
    UserDTO load();

    @Query("DELETE FROM UserDTO WHERE EMAIL = :email")
    void delete(String email);

    @Query("DELETE FROM UserDTO")
    public void deleteAll();
}
