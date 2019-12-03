package com.dleague.lakeshoreimporters.dtos.responsedto.apply_spent_rule_response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class Customer implements Serializable {

	@SerializedName("is_from_mailchimp")
	public int isFromMailchimp;

	@SerializedName("birthdate")
	public Object birthdate;

	@SerializedName("is_subscriber_given")
	public int isSubscriberGiven;

	@SerializedName("gender")
	public Object gender;

	@SerializedName("is_register_given")
	public int isRegisterGiven;

	@SerializedName("is_subscriber")
	public int isSubscriber;

	@SerializedName("promotional_credits")
	public int promotionalCredits;

	@SerializedName("created_at")
	public String createdAt;

	@SerializedName("is_first_order_given")
	public int isFirstOrderGiven;

	@SerializedName("points")
	public int points;

	@SerializedName("updated_at")
	public String updatedAt;

	@SerializedName("accepts_marketing")
	public int acceptsMarketing;

	@SerializedName("phone")
	public String phone;

	@SerializedName("credits")
	public double credits;

	@SerializedName("referral_code")
	public String referralCode;

	@SerializedName("name")
	public String name;

	@SerializedName("refer_by")
	public Object referBy;

	@SerializedName("is_from_shopify")
	public int isFromShopify;

	@SerializedName("id")
	public int id;

	@SerializedName("state")
	public String state;

	@SerializedName("customer_id")
	public long customerId;

	@SerializedName("email")
	public String email;

	@SerializedName("total_spent_credits")
	public int totalSpentCredits;

	@SerializedName("refund_credits")
	public int refundCredits;
}