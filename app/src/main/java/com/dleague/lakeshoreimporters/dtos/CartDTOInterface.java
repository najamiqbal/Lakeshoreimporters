package com.dleague.lakeshoreimporters.dtos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDTOInterface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartDTO... cartDTOS);

    @Update
    void update(CartDTO cartDTO);

    @Query("Select * FROM CartDTO WHERE CURSOR = :cursor")
    CartDTO load(String cursor);

    @Query("Select * FROM CartDTO")
    List<CartDTO> loadAll();

    @Query("DELETE FROM CartDTO WHERE CURSOR = :cursor")
    void delete(String cursor);

    @Query("DELETE FROM CartDTO")
    public void deleteAll();
}
