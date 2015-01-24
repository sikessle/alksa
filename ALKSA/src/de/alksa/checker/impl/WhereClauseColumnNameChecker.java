package de.alksa.checker.impl;

import java.util.HashSet;
import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.HierarchyToken;
import de.alksa.token.SelectStatementToken;

/**
 * Checks if only column names have been used, which are in the select column
 * list.
 */
public class WhereClauseColumnNameChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		if (subject.getWhereClause() == null) {
			return null;
		}

		Set<ColumnNameToken> learnedSelectAndWhereColumnNames = copyColumnNameTokens(learned
				.getColumnList());

		if (learned.getWhereClause() != null) {
			Set<ColumnNameToken> learnedWhereColumnNames = getInvolvedColumnNamesRecursive(copyHierarchyTokens(learned
					.getWhereClause()));
			learnedSelectAndWhereColumnNames.addAll(learnedWhereColumnNames);
		}

		Set<ColumnNameToken> subjectWhereColumnNames = getInvolvedColumnNamesRecursive(copyHierarchyTokens(subject
				.getWhereClause()));

		if (!isSubset(learnedSelectAndWhereColumnNames, subjectWhereColumnNames)) {
			return createLogEntry("columns used which are not stated in the column list");
		}

		return null;
	}

	private Set<ColumnNameToken> getInvolvedColumnNamesRecursive(
			Set<HierarchyToken> tokens) {

		Set<ColumnNameToken> result = new HashSet<>();

		for (HierarchyToken token : tokens) {
			result.addAll(copyRecursiveTokensOfType(token,
					ColumnNameToken.class));
		}

		return result;
	}

}
