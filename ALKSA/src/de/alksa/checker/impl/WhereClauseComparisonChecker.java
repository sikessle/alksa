package de.alksa.checker.impl;

import java.util.HashSet;
import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;

public class WhereClauseComparisonChecker extends WhereClauseChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		if (subject.getWhereClause() == null) {
			return null;
		}

		// Get all ComparisonTokens which involve a column.
		// Check if the columns are a subset of the Learned.selectColumnList.

		getRecursiveInvolvedColumnNames(copyComparisonFilterTokens(subject
				.getWhereClause()));

		return null;
	}

	private Set<ColumnNameToken> getRecursiveInvolvedColumnNames(
			Set<ComparisonFilterToken> tokens) {

		Set<ColumnNameToken> result = new HashSet<>();

		for (ComparisonFilterToken token : tokens) {
			result.addAll(getRecursiveColumnNameTokens(token));
		}

		return result;
	}

	private Set<ColumnNameToken> getRecursiveColumnNameTokens(
			ComparisonFilterToken token) {
		Set<ColumnNameToken> result = new HashSet<>();

		// left side
		if (token.getLeftPart() instanceof ColumnNameToken) {
			result.add((ColumnNameToken) token.getLeftPart());
		} else {

		}

		// right side
		if (token.getRightPart() instanceof ColumnNameToken) {
			result.add((ColumnNameToken) token.getRightPart());
		} else {

		}

		return result;
	}

	private ColumnNameToken getColumnNameTokenRecursive(Token token) {
		if (token instanceof ColumnNameToken) {
			return (ColumnNameToken) token;
		}
		return null;
	}

	// FilterToken

}
