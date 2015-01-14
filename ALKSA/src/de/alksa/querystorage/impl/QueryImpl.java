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

}
