package com.abhi1kush.registrationstore.valuebeans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {

	@JsonProperty("registraionNo")
	@JsonAlias("registrationNo")
	@NotBlank
	@Size(max = 32)
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	private String registrationNo;

	@NotBlank
	@Size(max = 20)
	@Pattern(regexp = "^[0-9]+$")
	private String rollNo;

	@NotBlank
	@Size(max = 120)
	@Pattern(regexp = "^[A-Za-z0-9 ]+$")
	private String university;

	@NotBlank
	@Size(max = 120)
	@Pattern(regexp = "^[A-Za-z0-9 ]+$")
	private String college;

	@NotBlank
	@Size(max = 60)
	@Pattern(regexp = "^[A-Za-z ]+$")
	private String firstName;

	@NotBlank
	@Size(max = 60)
	@Pattern(regexp = "^[A-Za-z ]+$")
	private String lastName;

	@NotBlank
	@Size(max = 120)
	@Email
	private String email;

	@NotBlank
	@Size(max = 15)
	@Pattern(regexp = "^[0-9]+$")
	private String phone;

	@NotBlank
	@Size(max = 80)
	@Pattern(regexp = "^[A-Za-z0-9 ]+$")
	private String department;

	@NotBlank
	@Pattern(regexp = "^[0-9]{4}$")
	private String batchYear;

	@NotBlank
	@Pattern(regexp = "^(1[0-2]|[1-9])$")
	private String semester;

	@NotBlank
	@Size(max = 80)
	@Pattern(regexp = "^[A-Za-z ]+$")
	private String city;

	@NotBlank
	@Size(max = 80)
	@Pattern(regexp = "^[A-Za-z ]+$")
	private String state;

	@NotBlank
	@Size(max = 80)
	@Pattern(regexp = "^[A-Za-z ]+$")
	private String country;

	@NotBlank
	@Pattern(regexp = "^[0-9]{6}$")
	private String pincode;

	@NotBlank
	@Size(max = 180)
	@Pattern(regexp = "^[A-Za-z0-9 ,.-]+$")
	private String addressLine1;

	@Size(max = 180)
	@Pattern(regexp = "^[A-Za-z0-9 ,.-]*$")
	private String addressLine2;

	@NotBlank
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
	private String dob;

	@NotBlank
	@Pattern(regexp = "^(MALE|FEMALE|OTHER)$")
	private String gender;

	@NotBlank
	@Size(max = 30)
	@Pattern(regexp = "^[A-Za-z0-9_]+$")
	private String idType;

	@NotBlank
	@Size(max = 50)
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	private String idNumber;

	@NotBlank
	@Size(max = 50)
	@Pattern(regexp = "^[A-Za-z0-9_]+$")
	private String sourceSystem;

	@Size(max = 60)
	@Pattern(regexp = "^[A-Za-z0-9_-]*$")
	private String referenceCode;

	@JsonProperty("current-sem-courses")
	@NotEmpty
	@Size(max = 20)
	@Valid
	private List<CourseBean> currentSemCourses;

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getBatchYear() {
		return batchYear;
	}

	public void setBatchYear(String batchYear) {
		this.batchYear = batchYear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public List<CourseBean> getCurrentSemCourses() {
		return currentSemCourses;
	}

	public void setCurrentSemCourses(List<CourseBean> currentSemCourses) {
		this.currentSemCourses = currentSemCourses;
	}
}
