package com.dleague.lakeshoreimporters.dtos.responsedto.apply_spent_rule_response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class Cart implements Serializable {

	@SerializedName("code")
	public String code;

	@SerializedName("applied_discount")
	public double appliedDiscount;

	@SerializedName("updated_at")
	public String updatedAt;

	@SerializedName("price_rule_id")
	public long priceRuleId;

	@SerializedName("created_at")
	public String createdAt;

	@SerializedName("id")
	public int id;

	@SerializedName("customer_id")
	public int customerId;

	@SerializedName("code_id")
	public long codeId;

	@SerializedName("token")
	public String token;
}