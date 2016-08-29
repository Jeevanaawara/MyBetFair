package com.mybetfair.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
	public SimpleStringProperty username = new SimpleStringProperty();
	public SimpleStringProperty token = new SimpleStringProperty();
	public SimpleStringProperty product = new SimpleStringProperty();
	public static User user;

	private User() {
	}

	public static User getSingleton() {
		if (user == null) {
			user = new User();
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SimpleStringProperty getUsernameProperty() {
		return username;
	}

	public void setUsername(String username) {
		this.username.set(username);
	}

	public String getToken() {
		return token.get();
	}

	public void setToken(String token) {
		this.token.set(token);
	}

	public String getProduct() {
		return product.get();
	}

	public void setProduct(String token) {
		this.product.set(token);
	}

}
