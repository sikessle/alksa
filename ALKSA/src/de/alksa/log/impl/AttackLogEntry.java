package de.alksa.log.impl;

import java.time.Instant;
import java.util.Objects;

import de.alksa.log.LogEntry;

public class AttackLogEntry implements LogEntry {

	private String query;
	private String database;
	private String databaseUser;
	private String violation;
	private Instant timestamp;

	public AttackLogEntry(String query, String database, String databaseUser,
			String violation, Instant timestamp) {

		Objects.requireNonNull(query);
		Objects.requireNonNull(database);
		Objects.requireNonNull(databaseUser);
		Objects.requireNonNull(violation);
		Objects.requireNonNull(timestamp);

		this.query = query;
		this.database = database;
		this.databaseUser = databaseUser;
		this.violation = violation;
		this.timestamp = timestamp;
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

	@Override
	public Instant getTimestamp() {
		return timestamp;
	}

}
