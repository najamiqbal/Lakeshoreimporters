package com.dleague.lakeshoreimporters.dtos.responsedto.customer_credit_response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class CustomerCreditDTO implements Serializable {

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