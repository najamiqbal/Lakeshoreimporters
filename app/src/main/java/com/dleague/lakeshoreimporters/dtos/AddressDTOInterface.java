package com.dleague.lakeshoreimporters.dtos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AddressDTOInterface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AddressDTO... addressDTOS);

    @Update
    void update(AddressDTO addressDTO);

    @Query("Select * FROM AddressDTO WHERE CURSOR = :id")
    AddressDTO load(String id);

    @Query("Select * FROM AddressDTO")
    List<AddressDTO> loadAll();

    @Query("DELETE FROM AddressDTO WHERE ADDRESS_ID = :id")
    void delete(String id);

    @Query("DELETE FROM AddressDTO")
    public void deleteAll();
}
