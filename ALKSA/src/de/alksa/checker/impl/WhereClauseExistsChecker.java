package de.alksa.checker.impl;

import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.token.FilterToken;

public class WhereClauseExistsChecker extends WhereClauseChecker {

	@Override
	protected LogEntry checkWhereClause(Set<FilterToken> subject,
			Set<FilterToken> learned) {

		if (!learned.isEmpty() && subject.isEmpty()) {
			return createLogEntry("where clause was removed");
		}
		return null;
	}

}
