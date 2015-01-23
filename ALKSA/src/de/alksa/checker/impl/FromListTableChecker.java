package de.alksa.checker.impl;

import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.TableNameToken;

public class FromListTableChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		Set<TableNameToken> subjectList = copyTableNameTokens(subject
				.getFromList());
		Set<TableNameToken> learnedList = copyTableNameTokens(learned
				.getFromList());

		if (isSubset(learnedList, subjectList)) {
			return null;
		}

		return createLogEntry("table names are not a subset");
	}
}
