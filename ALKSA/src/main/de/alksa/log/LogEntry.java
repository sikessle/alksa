package de.alksa.log;

import java.time.Instant;

public interface LogEntry {

	/**
	 * Query as String
	 */
	String getQuery();

	String getDatabase();

	String getDatabaseUser();

	String getViolation();

	/**
	 * Time stamp when the log entry was created
	 */
	Instant getTimestamp();

}