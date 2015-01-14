package de.alksa.querystorage;

import java.util.List;

public interface QueryStorage {
	
	void write(Query query);
	
	List<Query> read();
}
