package de.alksa.persistence;

import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.querystorage.Query;

public interface StorageDao {

	Set<LogEntry> getLogEntries();

	void saveLogEntries(Set<LogEntry> entries);

	Set<Query> getQueries();

	void saveQueries(Set<Query> queries);

	void close();

}
