package de.alksa.log;

import java.util.Set;

public interface Logger {

	void write(LogEntry entry);

	Set<LogEntry> read();

}