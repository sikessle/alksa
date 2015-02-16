package de.alksa.querystorage;

import java.util.Set;

public interface QueryStorage {

	void write(Query query);

	Set<Query> read();

}
