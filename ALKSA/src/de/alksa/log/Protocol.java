package de.alksa.log;

import java.util.List;

public interface Protocol {

	void write(LogEntry entry);

	List<LogEntry> read();

}