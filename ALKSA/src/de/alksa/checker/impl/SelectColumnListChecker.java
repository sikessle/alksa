package de.alksa.checker.impl;

import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;

public class SelectColumnListChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		Set<? extends Token> subjectList = subject.getColumnList()
				.getChildren();
		Set<? extends Token> learnedList = learned.getColumnList()
				.getChildren();

		if (isSubSet(subjectList, learnedList)) {
			return null;
		}

		return createLogEntry("not yet implemented");
	}

	private boolean isSubSet(Set<? extends Token> subject,
			Set<? extends Token> learned) {
		return learned.containsAll(subject);
	}
}
