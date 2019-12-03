package com.dleague.lakeshoreimporters.dtos.responsedto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdatePasswordResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("customer")
	private Customer customer;

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	public void setCustomer(Customer customer){
		this.customer = customer;
	}

	public Customer getCustomer(){
		return customer;
	}
}