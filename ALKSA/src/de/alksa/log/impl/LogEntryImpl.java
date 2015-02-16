package de.alksa.log.impl;

import java.time.Instant;
import java.util.Objects;

import de.alksa.log.LogEntry;

public class LogEntryImpl implements LogEntry {

	private String query;
	private String database;
	private String databaseUser;
	private String violation;
	private Instant timestamp;

	public LogEntryImpl(String query, String database, String databaseUser,
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((database == null) ? 0 : database.hashCode());
		result = prime * result
				+ ((databaseUser == null) ? 0 : databaseUser.hashCode());
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result
				+ ((violation == null) ? 0 : violation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LogEntryImpl other = (LogEntryImpl) obj;

		if (database == null) {
			if (other.database != null) {
				return false;
			}
		} else if (!database.equals(other.database)) {
			return false;
		}
		if (databaseUser == null) {
			if (other.databaseUser != null) {
				return false;
			}
		} else if (!databaseUser.equals(other.databaseUser)) {
			return false;
		}
		if (query == null) {
			if (other.query != null) {
				return false;
			}
		} else if (!query.equals(other.query)) {
			return false;
		}
		if (timestamp == null) {
			if (other.timestamp != null) {
				return false;
			}
		} else if (!timestamp.equals(other.timestamp)) {
			return false;
		}
		if (violation == null) {
			if (other.violation != null) {
				return false;
			}
		} else if (!violation.equals(other.violation)) {
			return false;
		}
		return true;
	}

}
