package de.alksa.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.alksa.log.LogEntry;
import de.alksa.persistence.StorageDao;
import de.alksa.querystorage.Query;

@Singleton
class Db4oStorageDao implements StorageDao {

	private EmbeddedObjectContainer db;
	private final String dbPath;

	@Inject
	public Db4oStorageDao(@Named("db4oPath") String dbPath) {
		this.dbPath = dbPath;
		open();
	}

	@Override
	public List<LogEntry> getLogEntries() {
		List<LogEntry> entries = new ArrayList<>();
		ObjectSet<LogEntry> resultSet = db.query(LogEntry.class);

		entries.addAll(resultSet);

		return entries;
	}

	@Override
	public void saveLogEntries(List<LogEntry> entries) {
		for (LogEntry entry : entries) {
			db.store(entry);
		}
	}

	@Override
	public List<Query> getQueries() {
		List<Query> queries = new ArrayList<>();
		ObjectSet<Query> resultSet = db.query(Query.class);

		queries.addAll(resultSet);

		return queries;
	}

	@Override
	public void saveQueries(List<Query> queries) {
		for (Query query : queries) {
			db.store(query);
		}
	}

	@Override
	public void close() {
		db.close();
	}

	@Override
	public void open() {
		db = Db4oEmbedded.openFile(dbPath);
	}
}
