package de.alksa.querystorage.impl;

import java.util.Objects;

import de.alksa.querystorage.Query;

public class QueryImpl implements Query {

	Object query;
	String database;
	String databaseUser;

	public QueryImpl(Object sqlQuery, String database, String databaseUser) {
		Objects.requireNonNull(sqlQuery);
		Objects.requireNonNull(database);
		Objects.requireNonNull(databaseUser);
		this.query = sqlQuery;
		this.database = database;
		this.databaseUser = databaseUser;
	}

	@Override
	public Object getQuery() {
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

}
