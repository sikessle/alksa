package de.alksa.querystorage.impl;

import java.util.List;
import java.util.Objects;

import de.alksa.querystorage.Query;
import de.alksa.token.Token;

public class QueryImpl implements Query {

	List<Token> query;
	String database;
	String databaseUser;

	public QueryImpl(List<Token> sqlQuery, String database, String databaseUser) {
		Objects.requireNonNull(sqlQuery);
		Objects.requireNonNull(database);
		Objects.requireNonNull(databaseUser);
		this.query = sqlQuery;
		this.database = database;
		this.databaseUser = databaseUser;
	}

	@Override
	public List<Token> getQuery() {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((database == null) ? 0 : database.hashCode());
		result = prime * result
				+ ((databaseUser == null) ? 0 : databaseUser.hashCode());
		result = prime * result + ((query == null) ? 0 : query.hashCode());
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
		QueryImpl other = (QueryImpl) obj;
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
		return true;
	}

}
