package de.alksa.checker.impl;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.SelectStatementToken;

public class SelectColumnListChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		// if isSubset() or ....
		return createLogEntry("not yet implemented");
	}

}
