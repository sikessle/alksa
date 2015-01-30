package de.alksa.checker.impl;

import java.util.HashSet;
import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.FilterToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;

public class WhereClauseComparisonChecker extends WhereClauseChecker {

	@Override
	protected LogEntry checkWhereClause(Set<FilterToken> subject,
			Set<FilterToken> learned) {

		if (subject.isEmpty()) {
			return null;
		}

		Set<ComparisonFilterToken> subjectComps = copyRecursiveTokensOfType(
				subject, ComparisonFilterToken.class);
		Set<ComparisonFilterToken> learnedComps = copyRecursiveTokensOfType(
				learned, ComparisonFilterToken.class);

		Set<Token> subjectColumns = getColumnsFromComparisons(subjectComps);
		Set<Token> learnedColumns = getColumnsFromComparisons(learnedComps);

		subjectColumns.removeAll(learnedColumns);

		if (!isSubset(legalColumnsForNewFilters, subjectColumns)) {
			return createLogEntry("illegal comparison added");
		}

		return null;
	}

	private Set<Token> getColumnsFromComparisons(
			Set<ComparisonFilterToken> comparisons) {
		Set<Token> comparisonParts = new HashSet<>();

		for (ComparisonFilterToken comp : comparisons) {
			comparisonParts.addAll(copyRecursiveTokensOfType(comp,
					ColumnNameToken.class));
			comparisonParts.addAll(copyRecursiveTokensOfType(comp,
					SelectStatementToken.class));
		}

		return comparisonParts;
	}
}
