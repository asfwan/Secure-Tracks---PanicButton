/****
 * This is the people class that defines the properties of the user
 * */

package com.sigtech.panicbutton.data;

public class People {

	private String email, name, username, phone, address, role,
			registration_date_time, activation_code;
	private int id;
	boolean activated = false, approved = false;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone_number() {
		return phone;
	}

	public void setPhone_number(String phone_number) {
		this.phone = phone_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRegistration_date_time() {
		return registration_date_time;
	}

	public void setRegistration_date_time(String registration_date_time) {
		this.registration_date_time = registration_date_time;
	}

	public String getActivation_code() {
		return activation_code;
	}

	public void setActivation_code(String activation_code) {
		this.activation_code = activation_code;
	}

}
