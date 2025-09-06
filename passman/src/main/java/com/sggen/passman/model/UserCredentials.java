package com.sggen.passman.model;

public class UserCredentials {
	public record userCredentials(
			String username,
			String password_hash,
			String password_salt
			) {}
}
