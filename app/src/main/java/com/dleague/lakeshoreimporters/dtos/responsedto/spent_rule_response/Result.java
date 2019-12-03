package com.dleague.lakeshoreimporters.dtos.responsedto.spent_rule_response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Result implements Serializable {

	@SerializedName("column_compare_value")
	public int columnCompareValue;

	@SerializedName("max_column_value")
	public String maxColumnValue;

	@SerializedName("applicable_credits")
	public int applicableCredits;

	@SerializedName("after_column_value")
	public int afterColumnValue;

	@SerializedName("min_column_value")
	public String minColumnValue;
}