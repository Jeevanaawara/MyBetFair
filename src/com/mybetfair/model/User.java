package com.mybetfair.model;

public class User {
	public String username;
	public String password;
	public String token;
	public static User user;
	
	private User(){
	}
	
	public static User getSingleton(){
		if(user == null){
			user = new User();
		}
		return user;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
