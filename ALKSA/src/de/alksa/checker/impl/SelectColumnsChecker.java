package de.alksa.checker.impl;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.querystorage.Query;

public class SelectColumnsChecker extends QueryChecker {

	@Override
	protected LogEntry check(Query subject, Query learned) {
		return createLogEntry(subject, "not yet implemented");
	}

}
