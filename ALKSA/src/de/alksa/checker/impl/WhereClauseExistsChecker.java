package de.alksa.checker.impl;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.SelectStatementToken;

public class WhereClauseExistsChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		if (learned.getWhereClause() != null) {
			if (subject.getWhereClause() == null) {
				return createLogEntry("no where clause found");
			}
		}

		return null;
	}

}
