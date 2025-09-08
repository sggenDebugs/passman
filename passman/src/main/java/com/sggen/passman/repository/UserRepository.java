package com.sggen.passman.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.sggen.passman.model.*;

public class UserRepository {
	/**
	 * Insert User Function
	 */
	public static void insertUser(Connection conn, String username, String passwordHash, String passwordSalt)
			throws SQLException {
		String sql = "INSERT INTO USERS (username, password_hash, password_salt) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			pstmt.setString(2, passwordHash);
			pstmt.setString(3, passwordSalt);
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("User '" + username + "' inserted successfully.");
			}
		}
	}
	/**
	 * get password_hash and password_salt by username
	 */
	public static UserCredentials getAuthData(Connection conn, String username) throws SQLException{
		String sql = "SELECT password_hash, password_salt FROM USERS WHERE username = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				// If a record is found, move to the first row.
				if (rs.next()) {
					// Retrieve the values from the result set.
					String stored_password_hash = rs.getString("password_hash");
					String stored_password_salt = rs.getString("password_salt");
					return new UserCredentials(username, stored_password_hash, stored_password_salt);
				}
			}
		}
		return null;
	}
}
