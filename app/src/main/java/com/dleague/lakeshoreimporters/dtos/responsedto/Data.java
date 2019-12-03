package com.dleague.lakeshoreimporters.dtos.responsedto;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable {

	@SerializedName("total_spent")
	private String totalSpent;

	@SerializedName("note")
	private Object note;

	@SerializedName("addresses")
	private List<Object> addresses;

	@SerializedName("last_order_name")
	private Object lastOrderName;

	@SerializedName("last_order_id")
	private Object lastOrderId;

	@SerializedName("tax_exempt")
	private boolean taxExempt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("multipass_identifier")
	private Object multipassIdentifier;

	@SerializedName("verified_email")
	private boolean verifiedEmail;

	@SerializedName("tags")
	private String tags;

	@SerializedName("orders_count")
	private int ordersCount;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("accepts_marketing")
	private boolean acceptsMarketing;

	@SerializedName("phone")
	private Object phone;

	@SerializedName("id")
	private long id;

	@SerializedName("state")
	private String state;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	public void setTotalSpent(String totalSpent){
		this.totalSpent = totalSpent;
	}

	public String getTotalSpent(){
		return totalSpent;
	}

	public void setNote(Object note){
		this.note = note;
	}

	public Object getNote(){
		return note;
	}

	public void setAddresses(List<Object> addresses){
		this.addresses = addresses;
	}

	public List<Object> getAddresses(){
		return addresses;
	}

	public void setLastOrderName(Object lastOrderName){
		this.lastOrderName = lastOrderName;
	}

	public Object getLastOrderName(){
		return lastOrderName;
	}

	public void setLastOrderId(Object lastOrderId){
		this.lastOrderId = lastOrderId;
	}

	public Object getLastOrderId(){
		return lastOrderId;
	}

	public void setTaxExempt(boolean taxExempt){
		this.taxExempt = taxExempt;
	}

	public boolean isTaxExempt(){
		return taxExempt;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setMultipassIdentifier(Object multipassIdentifier){
		this.multipassIdentifier = multipassIdentifier;
	}

	public Object getMultipassIdentifier(){
		return multipassIdentifier;
	}

	public void setVerifiedEmail(boolean verifiedEmail){
		this.verifiedEmail = verifiedEmail;
	}

	public boolean isVerifiedEmail(){
		return verifiedEmail;
	}

	public void setTags(String tags){
		this.tags = tags;
	}

	public String getTags(){
		return tags;
	}

	public void setOrdersCount(int ordersCount){
		this.ordersCount = ordersCount;
	}

	public int getOrdersCount(){
		return ordersCount;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setAcceptsMarketing(boolean acceptsMarketing){
		this.acceptsMarketing = acceptsMarketing;
	}

	public boolean isAcceptsMarketing(){
		return acceptsMarketing;
	}

	public void setPhone(Object phone){
		this.phone = phone;
	}

	public Object getPhone(){
		return phone;
	}

	public void setId(long id){
		this.id = id;
	}

	public long getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}