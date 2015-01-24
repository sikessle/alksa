package de.alksa.checker.impl;

import java.util.HashSet;
import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.SelectStatementToken;

public class WhereClauseComparisonChecker extends WhereClauseChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		if (subject.getWhereClause() == null) {
			return null;
		}

		// Get all ComparisonTokens which involve a column.
		// Check if the columns are a subset of the Learned.selectColumnList.

		Set<ColumnNameToken> learnedColumnNames = copyColumnNameTokens(learned
				.getColumnList());

		Set<ColumnNameToken> subjectComparisonColumnNames = getInvolvedColumnNamesRecursive(copyComparisonFilterTokens(subject
				.getWhereClause()));

		if (!isSubset(learnedColumnNames, subjectComparisonColumnNames)) {
			return createLogEntry("columns used which are not stated in the column list");
		}

		return null;
	}

	private Set<ColumnNameToken> getInvolvedColumnNamesRecursive(
			Set<ComparisonFilterToken> tokens) {

		Set<ColumnNameToken> result = new HashSet<>();

		for (ComparisonFilterToken token : tokens) {
			result.addAll(copyRecursiveTokensOfType(token,
					ColumnNameToken.class));
		}

		return result;
	}

}
