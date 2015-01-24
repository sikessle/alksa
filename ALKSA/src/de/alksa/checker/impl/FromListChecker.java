package de.alksa.checker.impl;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.SelectStatementToken;

public class FromListChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		if (learned.getFromList().equals(subject.getFromList())) {
			return null;
		}

		return createLogEntry("table names/subqueries do not match / implicit join");
	}
}
