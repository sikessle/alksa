package de.alksa.log.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import de.alksa.log.LogEntry;
import de.alksa.log.Protocol;
import de.alksa.persistence.StorageDao;

public class SimpleProtocol implements Protocol {

	private List<LogEntry> entries = new ArrayList<>();
	private StorageDao storage;
	
	@Inject
	public SimpleProtocol(StorageDao storage) {
		this.storage = storage;
		
		entries = storage.getProtocolEntries();
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
		storage.saveProtocolEntries(entries);
	}

	@Override
	public List<LogEntry> read() {
		return entries;
	}
}
