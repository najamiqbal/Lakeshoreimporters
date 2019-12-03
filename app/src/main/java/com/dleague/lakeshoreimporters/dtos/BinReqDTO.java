package com.dleague.lakeshoreimporters.dtos;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Generated("com.asif.gsonpojogenerator")
public class BinReqDTO implements Serializable {

    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    @SerializedName("email")
    public String email;

    @SerializedName("description")
    public String description;

    @SerializedName("location")
    public String location;

    @SerializedName("sheet")
    public String sheet;

    @SerializedName("action")
    public String action;

    @SerializedName("pickDate")
    public String pickDate;

    @SerializedName("agreement1")
    public String agreement1;

    @SerializedName("agreement2")
    public String agreement2;

    @SerializedName("agreement3")
    public String agreement3;

    @SerializedName("agreement4")
    public String agreement4;

}