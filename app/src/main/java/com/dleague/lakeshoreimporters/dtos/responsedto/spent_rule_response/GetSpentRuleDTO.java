package com.dleague.lakeshoreimporters.dtos.responsedto.spent_rule_response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetSpentRuleDTO implements Serializable {

	@SerializedName("is_credit_on_checkout")
	public String isCreditOnCheckout;

	@SerializedName("code")
	public Code code;

	@SerializedName("is_credit_on_cart")
	public String isCreditOnCart;

	@SerializedName("credits")
	public double credits;

	@SerializedName("status")
	public boolean status;
}