package com.dleague.lakeshoreimporters.dtos.responsedto.apply_spent_rule_response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class ApplySpentRulesDTO implements Serializable {

	@SerializedName("cart_value_to_apply_credit")
	public Object cartValueToApplyCredit;

	@SerializedName("code")
	public String code;

	@SerializedName("cart")
	public Cart cart;

	@SerializedName("status")
	public boolean status;

	@SerializedName("customer")
	public Customer customer;
}