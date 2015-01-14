package de.alksa.querystorage;

import java.util.List;

import de.alksa.token.Token;

public interface Query {
	
	List<Token> getQuery();
	
	String getDatabase();
	
	String getDatabaseUser();
}
