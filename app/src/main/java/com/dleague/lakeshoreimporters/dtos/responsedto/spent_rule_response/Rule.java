package com.dleague.lakeshoreimporters.dtos.responsedto.spent_rule_response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rule implements Serializable {

	@SerializedName("comment_text")
	public String commentText;

	@SerializedName("is_active")
	public int isActive;

	@SerializedName("created_at")
	public String createdAt;

	@SerializedName("rule")
	public Rule rule;

	@SerializedName("is_fixed")
	public int isFixed;

	@SerializedName("deleted_at")
	public Object deletedAt;

	@SerializedName("module_on")
	public String moduleOn;

	@SerializedName("rule_id")
	public int ruleId;

	@SerializedName("rule_type")
	public String ruleType;

	@SerializedName("updated_at")
	public String updatedAt;

	@SerializedName("time_upto")
	public int timeUpto;

	@SerializedName("id")
	public int id;

	@SerializedName("credit_value")
	public int creditValue;

	@SerializedName("column_value")
	public String columnValue;

	@SerializedName("campaign_id")
	public int campaignId;
}