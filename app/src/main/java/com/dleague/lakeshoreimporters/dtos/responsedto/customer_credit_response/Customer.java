package com.dleague.lakeshoreimporters.dtos.responsedto.customer_credit_response;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.asif.gsonpojogenerator")
public class Customer implements Serializable {

	@SerializedName("is_from_mailchimp")
	private int isFromMailchimp;

	@SerializedName("birthdate")
	private Object birthdate;

	@SerializedName("gender")
	private Object gender;

	@SerializedName("earned_credits")
	private List<Object> earnedCredits;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("is_first_order_given")
	private int isFirstOrderGiven;

	@SerializedName("points")
	private int points;

	@SerializedName("total_earned_credits")
	private int totalEarnedCredits;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("accepts_marketing")
	private int acceptsMarketing;

	@SerializedName("credits")
	private double credits;

	@SerializedName("refer_by")
	private Object referBy;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("credit_log")
	private List<CreditLogsDTO> creditLog;

	@SerializedName("email")
	private String email;

	@SerializedName("total_spent_credits")
	private int totalSpentCredits;

	@SerializedName("is_subscriber_given")
	private int isSubscriberGiven;

	@SerializedName("is_register_given")
	private int isRegisterGiven;

	@SerializedName("is_subscriber")
	private int isSubscriber;

	@SerializedName("promotional_credits")
	private int promotionalCredits;

	@SerializedName("phone")
	private Object phone;

	@SerializedName("spent_credits")
	private List<Object> spentCredits;

	@SerializedName("referral_code")
	private String referralCode;

	@SerializedName("name")
	private String name;

	@SerializedName("is_from_shopify")
	private int isFromShopify;

	@SerializedName("customer_id")
	private long customerId;

	@SerializedName("refund_credits")
	private int refundCredits;

	public void setIsFromMailchimp(int isFromMailchimp){
		this.isFromMailchimp = isFromMailchimp;
	}

	public int getIsFromMailchimp(){
		return isFromMailchimp;
	}

	public void setBirthdate(Object birthdate){
		this.birthdate = birthdate;
	}

	public Object getBirthdate(){
		return birthdate;
	}

	public void setGender(Object gender){
		this.gender = gender;
	}

	public Object getGender(){
		return gender;
	}

	public void setEarnedCredits(List<Object> earnedCredits){
		this.earnedCredits = earnedCredits;
	}

	public List<Object> getEarnedCredits(){
		return earnedCredits;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setIsFirstOrderGiven(int isFirstOrderGiven){
		this.isFirstOrderGiven = isFirstOrderGiven;
	}

	public int getIsFirstOrderGiven(){
		return isFirstOrderGiven;
	}

	public void setPoints(int points){
		this.points = points;
	}

	public int getPoints(){
		return points;
	}

	public void setTotalEarnedCredits(int totalEarnedCredits){
		this.totalEarnedCredits = totalEarnedCredits;
	}

	public int getTotalEarnedCredits(){
		return totalEarnedCredits;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setAcceptsMarketing(int acceptsMarketing){
		this.acceptsMarketing = acceptsMarketing;
	}

	public int getAcceptsMarketing(){
		return acceptsMarketing;
	}

	public void setCredits(int credits){
		this.credits = credits;
	}

	public double getCredits(){
		return credits;
	}

	public void setReferBy(Object referBy){
		this.referBy = referBy;
	}

	public Object getReferBy(){
		return referBy;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setCreditLog(List<CreditLogsDTO> creditLog){
		this.creditLog = creditLog;
	}

	public List<CreditLogsDTO> getCreditLog(){
		return creditLog;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setTotalSpentCredits(int totalSpentCredits){
		this.totalSpentCredits = totalSpentCredits;
	}

	public int getTotalSpentCredits(){
		return totalSpentCredits;
	}

	public void setIsSubscriberGiven(int isSubscriberGiven){
		this.isSubscriberGiven = isSubscriberGiven;
	}

	public int getIsSubscriberGiven(){
		return isSubscriberGiven;
	}

	public void setIsRegisterGiven(int isRegisterGiven){
		this.isRegisterGiven = isRegisterGiven;
	}

	public int getIsRegisterGiven(){
		return isRegisterGiven;
	}

	public void setIsSubscriber(int isSubscriber){
		this.isSubscriber = isSubscriber;
	}

	public int getIsSubscriber(){
		return isSubscriber;
	}

	public void setPromotionalCredits(int promotionalCredits){
		this.promotionalCredits = promotionalCredits;
	}

	public int getPromotionalCredits(){
		return promotionalCredits;
	}

	public void setPhone(Object phone){
		this.phone = phone;
	}

	public Object getPhone(){
		return phone;
	}

	public void setSpentCredits(List<Object> spentCredits){
		this.spentCredits = spentCredits;
	}

	public List<Object> getSpentCredits(){
		return spentCredits;
	}

	public void setReferralCode(String referralCode){
		this.referralCode = referralCode;
	}

	public String getReferralCode(){
		return referralCode;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIsFromShopify(int isFromShopify){
		this.isFromShopify = isFromShopify;
	}

	public int getIsFromShopify(){
		return isFromShopify;
	}

	public void setCustomerId(long customerId){
		this.customerId = customerId;
	}

	public long getCustomerId(){
		return customerId;
	}

	public void setRefundCredits(int refundCredits){
		this.refundCredits = refundCredits;
	}

	public int getRefundCredits(){
		return refundCredits;
	}
}