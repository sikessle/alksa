package de.alksa.checker.impl;

import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.SelectStatementToken;

public class FromListSubqueryChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		Set<SelectStatementToken> subjectSubqueries = copySelectStatementTokens(subject
				.getFromList());
		Set<SelectStatementToken> learnedSubqueries = copySelectStatementTokens(learned
				.getFromList());

		if (isSubset(learnedSubqueries, subjectSubqueries)) {
			return null;
		}

		return createLogEntry("subqueries do not match");
	}
}
