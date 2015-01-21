package de.alksa.log;

import java.time.Instant;

public interface LogEntry {

	String getQuery();

	String getDatabase();

	String getDatabaseUser();

	String getViolation();

	Instant getTimestamp();

}