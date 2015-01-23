package de.alksa.querystorage;

import de.alksa.token.SelectStatementToken;

public interface Query {

	public SelectStatementToken getSelectStatement();

	public String getQueryString();

	public String getDatabase();

	public String getDatabaseUser();

}