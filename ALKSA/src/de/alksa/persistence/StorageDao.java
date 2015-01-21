package de.alksa.persistence;

import java.util.List;

import de.alksa.log.LogEntry;
import de.alksa.querystorage.Query;

public interface StorageDao {

	List<LogEntry> getLogEntries();

	void saveLogEntries(List<LogEntry> entries);

	List<Query> getQueries();

	void saveQueries(List<Query> queries);
	
	void close();
	
	void open();

}
