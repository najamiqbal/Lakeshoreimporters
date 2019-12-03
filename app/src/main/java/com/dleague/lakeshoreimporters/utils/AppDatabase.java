package com.dleague.lakeshoreimporters.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.AddressDTO;
import com.dleague.lakeshoreimporters.dtos.AddressDTOInterface;
import com.dleague.lakeshoreimporters.dtos.CartDTO;
import com.dleague.lakeshoreimporters.dtos.CartDTOInterface;
import com.dleague.lakeshoreimporters.dtos.UserDTO;
import com.dleague.lakeshoreimporters.dtos.UserDTOInterface;

@Database(entities = {UserDTO.class, AddressDTO.class, CartDTO.class}, version = 3, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDTOInterface userDTOInterface();
    public abstract AddressDTOInterface addressDTOInterface();
    public abstract CartDTOInterface cartDTOInterface();

    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    context.getResources().getString(R.string.app_name))
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }


}
