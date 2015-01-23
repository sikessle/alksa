package de.alksa.checker.impl;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.SelectStatementToken;

/**
 * Checks calculations which involve a column or function
 */
public class SelectColumnListCalculationChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		subject.getColumnList();

		return null;
	}

}
