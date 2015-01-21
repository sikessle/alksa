package de.alksa.checker.impl;

import java.util.List;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.Token;

public class SelectColumnsChecker extends QueryChecker {

	@Override
	protected LogEntry check(List<Token> subject, List<Token> learned) {
		return null; // createLogEntry("not yet implemented");
	}

}
