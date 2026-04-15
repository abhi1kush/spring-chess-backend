package com.abhi1kush.registrationstore.valuebeans;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UnitBean {

	@NotBlank
	@Size(max = 120)
	@Pattern(regexp = "^[A-Za-z0-9 .,-]+$")
	private String name;

	@JsonProperty("weitage")
	@Min(0)
	@Max(100)
	private Integer weitage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeitage() {
		return weitage;
	}

	public void setWeitage(Integer weitage) {
		this.weitage = weitage;
	}
}
