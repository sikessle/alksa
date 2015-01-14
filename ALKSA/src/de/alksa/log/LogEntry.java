package de.alksa.log;

public interface LogEntry {

	String getQuery();

	String getDatabase();

	String getDatabaseUser();

	String getViolation();

}