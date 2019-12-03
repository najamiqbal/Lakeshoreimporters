package com.dleague.lakeshoreimporters.dtos.responsedto.customer_credit_response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class CreditLogsDTO implements Serializable {

	@SerializedName("rule_id")
	private int ruleId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("credits")
	private int credits;

	@SerializedName("credit_from")
	private Object creditFrom;

	@SerializedName("expiry_date")
	private Object expiryDate;

	@SerializedName("credit_type_id")
	private int creditTypeId;

	@SerializedName("used_credits")
	private int usedCredits;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("comment")
	private String comment;

	@SerializedName("id")
	private int id;

	@SerializedName("customer_id")
	private int customerId;

	@SerializedName("deleted_at")
	private Object deletedAt;

	public void setRuleId(int ruleId){
		this.ruleId = ruleId;
	}

	public int getRuleId(){
		return ruleId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCredits(int credits){
		this.credits = credits;
	}

	public int getCredits(){
		return credits;
	}

	public void setCreditFrom(Object creditFrom){
		this.creditFrom = creditFrom;
	}

	public Object getCreditFrom(){
		return creditFrom;
	}

	public void setExpiryDate(Object expiryDate){
		this.expiryDate = expiryDate;
	}

	public Object getExpiryDate(){
		return expiryDate;
	}

	public void setCreditTypeId(int creditTypeId){
		this.creditTypeId = creditTypeId;
	}

	public int getCreditTypeId(){
		return creditTypeId;
	}

	public void setUsedCredits(int usedCredits){
		this.usedCredits = usedCredits;
	}

	public int getUsedCredits(){
		return usedCredits;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCustomerId(int customerId){
		this.customerId = customerId;
	}

	public int getCustomerId(){
		return customerId;
	}

	public void setDeletedAt(Object deletedAt){
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}
}