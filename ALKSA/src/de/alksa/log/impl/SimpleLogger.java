package de.alksa.log.impl;

import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.alksa.log.LogEntry;
import de.alksa.log.Logger;
import de.alksa.persistence.StorageDao;

@Singleton
class SimpleLogger implements Logger {

	private Set<LogEntry> entries;
	private StorageDao storage;

	@Inject
	public SimpleLogger(StorageDao storage) {
		this.storage = storage;
		entries = storage.getLogEntries();
	}

	@Override
	public void write(LogEntry entry) {
		if (entry == null) {
			return;
		}
		entries.add(entry);
		persist();
	}

	private void persist() {
		storage.saveLogEntries(entries);
	}

	@Override
	public Set<LogEntry> read() {
		return entries;
	}
}
