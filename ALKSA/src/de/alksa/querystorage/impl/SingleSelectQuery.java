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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((database == null) ? 0 : database.hashCode());
		result = prime * result
				+ ((databaseUser == null) ? 0 : databaseUser.hashCode());
		result = prime * result
				+ ((queryString == null) ? 0 : queryString.hashCode());
		result = prime * result + ((select == null) ? 0 : select.hashCode());
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
		SingleSelectQuery other = (SingleSelectQuery) obj;
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
		if (queryString == null) {
			if (other.queryString != null) {
				return false;
			}
		} else if (!queryString.equals(other.queryString)) {
			return false;
		}
		if (select == null) {
			if (other.select != null) {
				return false;
			}
		} else if (!select.equals(other.select)) {
			return false;
		}
		return true;
	}

}
