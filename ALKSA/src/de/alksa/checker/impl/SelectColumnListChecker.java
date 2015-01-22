package de.alksa.checker.impl;

import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;
import de.alksa.util.TypeUtility;

public class SelectColumnListChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		Set<? extends Token> subjectList = TypeUtility.getAllTokensOfType(
				subject.getColumnList().getChildren(), ColumnNameToken.class);
		Set<? extends Token> learnedList = TypeUtility.getAllTokensOfType(
				learned.getColumnList().getChildren(), ColumnNameToken.class);

		if (containsAsterisk(learnedList) || isSubSet(subjectList, learnedList)) {
			return null;
		}

		return createLogEntry("column list is not a subset");
	}

	private boolean containsAsterisk(Set<? extends Token> learned) {
		return learned.contains(new ColumnNameToken("*"));
	}

	private boolean isSubSet(Set<? extends Token> subject,
			Set<? extends Token> learned) {
		return learned.containsAll(subject);
	}
}
