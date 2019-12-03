package com.dleague.lakeshoreimporters.dtos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "UserDTO")
public class UserDTO {
    @PrimaryKey(autoGenerate = true)
    Long uid;

    @ColumnInfo(name = "FIRSTNAME")
    public String firstName;

    @ColumnInfo(name = "LASTNAME")
    public String lastName;

    @ColumnInfo(name = "EMAIL")
    public String email;

    @ColumnInfo(name = "PASSWORD")
    public String password;

}
