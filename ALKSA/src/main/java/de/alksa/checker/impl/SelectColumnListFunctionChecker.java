package de.alksa.checker.impl;

import java.util.HashSet;
import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.FunctionToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.util.TypeUtil;

/**
 * BOUND: AVG(col1): col1 is "bound" UNBOUND: col2: col2 is "unbound"<br>
 * Column names: learned-bound must NOT be in subject-unbound<br>
 * Functions: functions of subject must either have only unbound column names or
 * must be the same function as learned with the same column
 */
class SelectColumnListFunctionChecker extends QueryChecker {

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		Set<FunctionToken> learnedFuncs = copyFunctionTokens(learned
				.getColumnList());
		Set<FunctionToken> subjectFuncs = copyFunctionTokens(subject
				.getColumnList());

		Set<ColumnNameToken> learnedBound = getBoundColumns(learnedFuncs);

		Set<ColumnNameToken> learnedUnbound = copyColumnNameTokens(learned
				.getColumnList());
		Set<ColumnNameToken> subjectUnbound = copyColumnNameTokens(subject
				.getColumnList());

		Set<ColumnNameToken> learnedBoundIntersectSubjectUnbound = new HashSet<>(
				learnedBound);
		learnedBoundIntersectSubjectUnbound.retainAll(subjectUnbound);

		if (containsAsterisk(learnedUnbound)) {
			return null;
		} else if (learnedBoundIntersectSubjectUnbound.size() > 0) {
			return createLogEntry("column was learned with function but appears now without");
		} else if (!functionsAreSubsetOrAppliedToUnboundColumns(subjectFuncs,
				learnedFuncs, learnedUnbound)) {
			return createLogEntry("functions do not match");
		}

		return null;
	}

	private boolean functionsAreSubsetOrAppliedToUnboundColumns(
			Set<FunctionToken> subjectFuncs, Set<FunctionToken> learnedFuncs,
			Set<ColumnNameToken> learnedUnbound) {

		Set<FunctionToken> cleanedSubjectFuncs = new HashSet<>(subjectFuncs);
		Set<ColumnNameToken> funcColumns;

		for (FunctionToken func : subjectFuncs) {
			funcColumns = copyColumnNameTokens(func.getChildren());
			funcColumns.removeAll(learnedUnbound);

			if (funcColumns.isEmpty()) {
				cleanedSubjectFuncs.remove(func);
			}
		}

		return learnedFuncs.containsAll(cleanedSubjectFuncs);
	}

	private Set<ColumnNameToken> getBoundColumns(Set<FunctionToken> tokens) {
		Set<ColumnNameToken> boundColumns = new HashSet<>();
		for (FunctionToken func : tokens) {
			boundColumns.add(TypeUtil.getFirstTokenOfType(func.getChildren(),
					ColumnNameToken.class));
		}

		return boundColumns;
	}
}
