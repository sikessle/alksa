package de.alksa.querystorage.impl;

import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.alksa.persistence.StorageDao;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

@Singleton
class SimpleQueryStorage implements QueryStorage {

	private StorageDao storage;
	private Set<Query> queries;

	@Inject
	public SimpleQueryStorage(StorageDao storage) {
		this.storage = storage;
		queries = storage.getQueries();
	}

	@Override
	public void write(Query query) {
		if (query == null) {
			return;
		}
		queries.add(query);
		persist();
	}

	private void persist() {
		storage.saveQueries(queries);
	}

	@Override
	public Set<Query> read() {
		return queries;
	}
}
