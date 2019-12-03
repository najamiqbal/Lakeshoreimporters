package com.dleague.lakeshoreimporters.dtos.responsedto.spent_rule_response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Relation implements Serializable {

	@SerializedName("symbol")
	public String symbol;

	@SerializedName("updated_at")
	public String updatedAt;

	@SerializedName("name")
	public String name;

	@SerializedName("created_at")
	public String createdAt;

	@SerializedName("id")
	public int id;

	@SerializedName("display_name")
	public String displayName;
}