package de.alksa.log.impl;

import de.alksa.log.LogEntry;

public class AttackLogEntry implements LogEntry {

	private String query;
	private String database;
	private String databaseUser;
	private String violation;

	public AttackLogEntry(String query, String database, String databaseUser,
			String cause) {

		this.query = query == null ? "" : query;
		this.database = database == null ? "" : database;
		this.databaseUser = databaseUser == null ? "" : databaseUser;
		this.violation = cause == null ? "" : cause;
	}

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public String getDatabase() {
		return database;
	}

	@Override
	public String getDatabaseUser() {
		return databaseUser;
	}

	@Override
	public String getViolation() {
		return violation;
	}

}
