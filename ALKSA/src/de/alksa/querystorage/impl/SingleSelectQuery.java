package de.alksa.querystorage.impl;

import java.util.Objects;

import de.alksa.querystorage.Query;
import de.alksa.token.SelectStatementToken;

public class SingleSelectQuery implements Query {

	private SelectStatementToken select;
	private String queryString;
	private String database;
	private String databaseUser;

	public SingleSelectQuery(SelectStatementToken select, String queryString,
			String database, String databaseUser) {
		Objects.requireNonNull(queryString);
		Objects.requireNonNull(database);
		Objects.requireNonNull(databaseUser);
		Objects.requireNonNull(select);

		this.select = select;
		this.queryString = queryString;
		this.database = database;
		this.databaseUser = databaseUser;
	}

	@Override
	public SelectStatementToken getSelectStatement() {
		return select;
	}

	@Override
	public String getQueryString() {
		return queryString;
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
