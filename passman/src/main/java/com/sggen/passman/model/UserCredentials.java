package com.sggen.passman.model;

public record UserCredentials(String username, String password_hash, String password_salt) {
}
