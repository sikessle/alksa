package de.alksa.checker.impl;

import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.SelectStatementToken;

public class SelectColumnListNameChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		Set<ColumnNameToken> subjectList = copyColumnNameTokens(subject
				.getColumnList());
		Set<ColumnNameToken> learnedList = copyColumnNameTokens(learned
				.getColumnList());

		if (containsAsterisk(learnedList) || isSubset(learnedList, subjectList)) {
			return null;
		}

		return createLogEntry("column names are not a subset");
	}
}
