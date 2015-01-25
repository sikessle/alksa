package de.alksa.checker.impl;

import java.util.HashSet;
import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.FilterToken;
import de.alksa.token.HierarchyToken;

/**
 * Checks if subject uses only column names which are:<br>
 * 1. in the learned where statement<br>
 * 2. OR visible in select column list AND not already in the learned where
 * statement
 */
public class WhereClauseColumnNameChecker extends WhereClauseChecker {

	@Override
	protected LogEntry checkWhereClause(Set<FilterToken> subject,
			Set<FilterToken> learned) {

		if (subject.isEmpty()) {
			return null;
		}

		// set of learned where columns
		Set<ColumnNameToken> learnedWhereColumns;
		if (learned.isEmpty()) {
			learnedWhereColumns = new HashSet<>();
		} else {
			learnedWhereColumns = getInvolvedColumnNamesRecursive(copyHierarchyTokens(learned));
		}

		// set of subject where columns
		Set<ColumnNameToken> subjectWhereColumns = getInvolvedColumnNamesRecursive(copyHierarchyTokens(subject));

		// if:
		// subject where is subset of learned where: then return null
		if (isSubset(learnedWhereColumns, subjectWhereColumns)) {
			return null;
		} else {
			// else:
			// remove all learnedWhereColumns from subject to get the rest
			Set<ColumnNameToken> subjectNoSubsetColumns = subjectWhereColumns;
			subjectNoSubsetColumns.removeAll(learnedWhereColumns);

			// set of learned select columns
			Set<ColumnNameToken> allowedColumns = copyColumnNameTokens(learnedColumnList);
			// subtract from learned select columns the learned where statements
			// =: allowedColumns
			allowedColumns.removeAll(learnedWhereColumns);
			// if:
			// subject where is subset of allowed columns: then return null
			if (isSubset(allowedColumns, subjectNoSubsetColumns)) {
				return null;
			} else {
				// else: return error.
				return createLogEntry("used columns which are already in use or not visible");
			}

		}
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
