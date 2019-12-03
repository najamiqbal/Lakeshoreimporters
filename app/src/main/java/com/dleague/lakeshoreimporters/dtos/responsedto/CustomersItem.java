package com.dleague.lakeshoreimporters.dtos.responsedto;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.asif.gsonpojogenerator")
public class CustomersItem implements Serializable {

	@SerializedName("total_spent")
	public String totalSpent;

	@SerializedName("note")
	public Object note;

	@SerializedName("addresses")
	public List<Object> addresses;

	@SerializedName("last_order_name")
	public Object lastOrderName;

	@SerializedName("last_order_id")
	public Object lastOrderId;

	@SerializedName("tax_exempt")
	public boolean taxExempt;

	@SerializedName("created_at")
	public String createdAt;

	@SerializedName("last_name")
	public String lastName;

	@SerializedName("multipass_identifier")
	public Object multipassIdentifier;

	@SerializedName("verified_email")
	public boolean verifiedEmail;

	@SerializedName("tags")
	public String tags;

	@SerializedName("accepts_marketing_updated_at")
	public String acceptsMarketingUpdatedAt;

	@SerializedName("orders_count")
	public int ordersCount;

	@SerializedName("updated_at")
	public String updatedAt;

	@SerializedName("accepts_marketing")
	public boolean acceptsMarketing;

	@SerializedName("phone")
	public Object phone;

	@SerializedName("admin_graphql_api_id")
	public String adminGraphqlApiId;

	@SerializedName("currency")
	public String currency;

	@SerializedName("id")
	public long id;

	@SerializedName("state")
	public String state;

	@SerializedName("marketing_opt_in_level")
	public Object marketingOptInLevel;

	@SerializedName("first_name")
	public String firstName;

	@SerializedName("email")
	public String email;
}