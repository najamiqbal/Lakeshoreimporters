package com.dleague.lakeshoreimporters.dtos.responsedto;

import java.io.Serializable;
import java.util.List;

import com.dleague.lakeshoreimporters.dtos.AddressDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCheckoutResponseDTO implements Serializable {

	public String totalPrice;
	public boolean requiresShipping;
	public AddressDTO shippingAddress;
	public List<ItemsItem> items;
}