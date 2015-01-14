package de.alksa.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.alksa.log.LogEntry;
import de.alksa.persistence.StorageDao;

public class Db4oStorageDao implements StorageDao {

	private final EmbeddedObjectContainer db;

	@Inject
	public Db4oStorageDao(@Named("db4oPath") String dbPath) {
		db = Db4oEmbedded.openFile(dbPath);
	}

	@Override
	public List<LogEntry> getProtocolEntries() {
		List<LogEntry> entries = new ArrayList<>();
		ObjectSet<LogEntry> resultSet = db.query(LogEntry.class);

		entries.addAll(resultSet);

		return entries;
	}

	@Override
	public void saveProtocolEntries(List<LogEntry> entries) {
		for (LogEntry entry : entries) {
			db.store(entry);
		}
	}
}
