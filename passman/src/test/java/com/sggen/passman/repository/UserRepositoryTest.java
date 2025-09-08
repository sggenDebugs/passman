package com.sggen.passman.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sggen.passman.model.UserCredentials;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserRepositoryTest {
	@Mock
	private Connection mockConnection;
	@Mock
	private PreparedStatement mockPreparedStatement;
	@Mock
    private ResultSet mockResultSet;


	@BeforeEach
	public void setUp() throws SQLException {
		// Initializes the mock objects.
		MockitoAnnotations.openMocks(this);
		// Configure the mock connection to return our mock PreparedStatement.
		// This simulates a real database connection preparing a statement.
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		// Configure the mock prepared statement to simulate an update.
		when(mockPreparedStatement.executeUpdate()).thenReturn(1);
	}

	@DisplayName("Test Insert User to Mock Database")
	@ParameterizedTest
	@CsvFileSource(resources = "/insertUser.csv")
	void testInsertUser(String username, String passwordHash, String salt) throws SQLException {

		UserRepository.insertUser(mockConnection, username, passwordHash, salt);

		verify(mockConnection)
				.prepareStatement("INSERT INTO USERS (username, password_hash, password_salt) VALUES (?, ?, ?)");
		verify(mockPreparedStatement).setString(1, username);
		verify(mockPreparedStatement).setString(2, passwordHash);
		verify(mockPreparedStatement).setString(3, salt);
		verify(mockPreparedStatement, times(1)).executeUpdate();

	}

	@Test
    void testGetAuthData_userExists() throws SQLException {
        // Define the test data we expect the mock ResultSet to "return".
        String expectedPasswordHash = "hashed_password_123";
        String expectedPasswordSalt = "random_salt_abc";

        // Configure the mock connection to return our mock PreparedStatement.
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Configure the mock PreparedStatement to return our mock ResultSet.
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Configure the mock ResultSet to simulate having a row with data.
        // The first call to rs.next() will return true, simulating that a record was found.
        when(mockResultSet.next()).thenReturn(true, false);

        // Configure the mock ResultSet to return our expected data when queried.
        when(mockResultSet.getString("password_hash")).thenReturn(expectedPasswordHash);
        when(mockResultSet.getString("password_salt")).thenReturn(expectedPasswordSalt);

        // Call the method to be tested.
        UserCredentials credentials = UserRepository.getAuthData(mockConnection, "existinguser");

        // --- Assertions ---
        // Verify that the method returned a non-null object.
        assertNotNull(credentials);
        // Verify that the returned object contains the correct password hash and salt.
        assertEquals(expectedPasswordHash, credentials.getPasswordHash());
        assertEquals(expectedPasswordSalt, credentials.getPasswordSalt());

        // --- Verification ---
        // Verify that the PreparedStatement was created with the correct SQL.
        verify(mockConnection).prepareStatement("SELECT password_hash, password_salt FROM USERS WHERE username = ?");
        // Verify that the username was set correctly as a parameter.
        verify(mockPreparedStatement).setString(1, "existinguser");
        // Verify that the query was executed exactly once.
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    void testGetAuthData_userDoesNotExist() throws SQLException {
        // Configure the mock connection to return our mock PreparedStatement.
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Configure the mock PreparedStatement to return our mock ResultSet.
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Configure the mock ResultSet to simulate no rows being returned.
        // The first call to rs.next() will return false.
        when(mockResultSet.next()).thenReturn(false);

        // Call the method to be tested.
        UserCredentials credentials = UserRepository.getAuthData(mockConnection, "nonexistentuser");

        // --- Assertions ---
        // Verify that the method returned null, as no user was found.
        assertNull(credentials);
        
        // --- Verification ---
        // Verify that the query was executed exactly once.
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

}
