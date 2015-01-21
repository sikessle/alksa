package de.alksa.log;

import java.util.List;

public interface Logger {

	void write(LogEntry entry);

	List<LogEntry> read();

}