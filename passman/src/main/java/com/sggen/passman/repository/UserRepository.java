package com.sggen.passman.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.sggen.passman.model.*;

public class UserRepository {
	private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
	private static final String USER = "PASS_MAN";
	private static final String PASS = "KawaiiKitty2425";
	/**
	 * User inputs
	 */
	private String username;
	private String password_hash;
	private String password_salt;

	/**
	 * Insert User Function
	 */
	private static void insertUser(Connection conn, String username, String passwordHash, String passwordSalt)
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
	 * Constructor that can insert User to the database.
	 */
	UserRepository(String inputUsername, String input_password_hash, String input_password_salt) throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
			this.username = inputUsername;
			this.password_hash = input_password_hash;
			this.password_salt = input_password_salt;
			insertUser(conn, this.username, this.password_hash, this.password_salt);
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
					return new UserCredentials(null, stored_password_hash, stored_password_salt);
				}
			}
		}
		return null;
	}
}
