package com.dleague.lakeshoreimporters.dtos.responsedto;

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
public class ItemsItem implements Serializable {
	public int quantity;
	public String price;
	public String id;
	public String tags;
}