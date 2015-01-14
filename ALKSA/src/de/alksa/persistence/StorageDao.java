package de.alksa.persistence;

import java.util.List;

import de.alksa.log.LogEntry;

public interface StorageDao {

	List<LogEntry> getProtocolEntries();

	void saveProtocolEntries(List<LogEntry> entries);

}
