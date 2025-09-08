package com.sggen.passman.model;

public class UserCredentials {
    private String passwordHash;
    private String passwordSalt;

    public UserCredentials(String username, String passwordHash, String passwordSalt) {
    	this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }
    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }
}
