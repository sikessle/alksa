package de.alksa.log;

public interface LogEntry {

	public abstract String getQuery();

	public abstract String getDatabase();

	public abstract String getDatabaseUser();

	public abstract String getViolation();

}