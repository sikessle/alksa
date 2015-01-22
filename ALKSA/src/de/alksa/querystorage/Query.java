package de.alksa.querystorage;

import java.util.Set;

import de.alksa.token.Token;

public interface Query {

	Set<Token> getQuery();

	String getDatabase();

	String getDatabaseUser();

	String getQueryString();
}
