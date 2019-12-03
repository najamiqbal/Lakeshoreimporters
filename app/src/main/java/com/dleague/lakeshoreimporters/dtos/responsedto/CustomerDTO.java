package com.dleague.lakeshoreimporters.dtos.responsedto;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class CustomerDTO implements Serializable {

	@SerializedName("customers")
	public List<CustomersItem> customers;
}