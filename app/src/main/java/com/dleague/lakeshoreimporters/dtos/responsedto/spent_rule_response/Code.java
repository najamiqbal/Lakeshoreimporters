package com.dleague.lakeshoreimporters.dtos.responsedto.spent_rule_response;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class Code implements Serializable {

	@SerializedName("result")
	public List<ResultItem> result;

	@SerializedName("total_credits")
	public double totalCredits;

	@SerializedName("rules")
	public List<RulesItem> rules;
}