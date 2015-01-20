package de.alksa.querystorage.impl;

import java.util.List;

import com.google.inject.Inject;

import de.alksa.persistence.StorageDao;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

class SimpleQueryStorage implements QueryStorage {

	private StorageDao storage;
	private List<Query> queries;

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
	public List<Query> read() {
		return queries;
	}
}
