package de.alksa.querystorage;

public interface Query {
	
	Object getQuery();
	
	String getDatabase();
	
	String getDatabaseUser();
}
