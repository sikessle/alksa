package de.alksa.checker.impl;

import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.JoinToken;
import de.alksa.token.SelectStatementToken;

public class FromListJoinChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		Set<JoinToken> subjectJoins = copyJoinTokens(subject.getFromList());
		Set<JoinToken> learnedJoins = copyJoinTokens(learned.getFromList());

		if (learnedJoins.equals(subjectJoins)) {
			return null;
		}

		return createLogEntry("joins do not match");
	}
}
