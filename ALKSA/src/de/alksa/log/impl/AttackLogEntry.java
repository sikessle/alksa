package de.alksa.log.impl;

import java.util.Objects;

import de.alksa.log.LogEntry;

public class AttackLogEntry implements LogEntry {

	private String query;
	private String database;
	private String databaseUser;
	private String violation;

	public AttackLogEntry(String query, String database, String databaseUser,
			String violation) {

		Objects.requireNonNull(query);
		Objects.requireNonNull(database);
		Objects.requireNonNull(databaseUser);
		Objects.requireNonNull(violation);

		this.query = query;
		this.database = database;
		this.databaseUser = databaseUser;
		this.violation = violation;
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
