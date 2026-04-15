package com.abhi1kush.registrationstore.valuebeans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CourseBean {

	@NotBlank
	@Size(max = 20)
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	private String courseCode;

	@JsonProperty("corseName")
	@JsonAlias("courseName")
	@NotBlank
	@Size(max = 150)
	@Pattern(regexp = "^[A-Za-z0-9 .,-]+$")
	private String corseName;

	@JsonProperty("Units")
	@NotEmpty
	@Size(max = 20)
	@Valid
	private List<UnitBean> units;

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCorseName() {
		return corseName;
	}

	public void setCorseName(String corseName) {
		this.corseName = corseName;
	}

	public List<UnitBean> getUnits() {
		return units;
	}

	public void setUnits(List<UnitBean> units) {
		this.units = units;
	}
}
