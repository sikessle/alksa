package de.alksa.checker.impl;

import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.SelectStatementToken;

public class SelectColumnListSubqueryChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		Set<SelectStatementToken> subjectSubqueries = copySelectStatementTokens(subject
				.getColumnList());
		Set<SelectStatementToken> learnedSubqueries = copySelectStatementTokens(learned
				.getColumnList());

		if (isSubset(learnedSubqueries, subjectSubqueries)) {
			return null;
		}

		return createLogEntry("functions do not match");
	}
}
