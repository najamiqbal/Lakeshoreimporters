package com.dleague.lakeshoreimporters.dtos.responsedto.spent_rule_response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RulesItem implements Serializable {

	@SerializedName("result")
	public Result result;

	@SerializedName("rule")
	public Rule rule;

	@SerializedName("applicable_credits")
	public double applicableCredits;

	@SerializedName("is_eligible")
	public boolean isEligible;
}