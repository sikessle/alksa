package de.alksa.persistence.impl;

import java.util.HashSet;
import java.util.Set;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.alksa.log.LogEntry;
import de.alksa.persistence.StorageDao;
import de.alksa.querystorage.Query;

@Singleton
class Db4oStorageDao implements StorageDao {

	private static final int DEPTH = 15;
	private EmbeddedObjectContainer db;
	private final String dbPath;

	@Inject
	public Db4oStorageDao(@Named("db4oPath") String dbPath) {
		this.dbPath = dbPath;
		open();
	}

	private final void open() {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		configuration.common().activationDepth(DEPTH);
		configuration.common().updateDepth(DEPTH);
		db = Db4oEmbedded.openFile(configuration, dbPath);
	}

	@Override
	public Set<LogEntry> getLogEntries() {
		Set<LogEntry> entries = new HashSet<>();
		ObjectSet<LogEntry> resultSet = db.query(LogEntry.class);

		entries.addAll(resultSet);

		return entries;
	}

	@Override
	public void saveLogEntries(Set<LogEntry> entries) {
		for (LogEntry entry : entries) {
			db.store(entry);
		}
	}

	@Override
	public Set<Query> getQueries() {
		Set<Query> queries = new HashSet<>();
		ObjectSet<Query> resultSet = db.query(Query.class);

		queries.addAll(resultSet);

		return queries;
	}

	@Override
	public void saveQueries(Set<Query> queries) {
		for (Query query : queries) {
			db.store(query);
		}
	}

	@Override
	public void close() {
		db.close();
	}

}
