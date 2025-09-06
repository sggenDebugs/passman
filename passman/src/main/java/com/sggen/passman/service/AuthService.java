package com.sggen.passman.service;
import com.sggen.passman.model.UserCredentials;

public class AuthService {
	public boolean register(String username, String password) {
		return false;
	}
	public boolean login(String username, String password) {
		return false;
	}
	
	private UserCredentials hashPassword(String username, String password) {
		String salt = "";
		return new UserCredentials(username, password, salt);
	}
	
	private boolean verifyPassword(String password, String storedHash, String storedSalt) {
		return false;
	}
}
