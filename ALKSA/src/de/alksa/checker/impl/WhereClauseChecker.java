package de.alksa.checker.impl;

import java.util.HashSet;
import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.FilterToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;

public abstract class WhereClauseChecker extends QueryChecker {

	protected Set<? extends Token> subjectColumnList;
	protected Set<? extends Token> learnedColumnList;
	/**
	 * Including subqueries
	 */
	protected Set<Token> legalColumnsForNewFilters;

	@Override
	protected LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned) {

		Set<FilterToken> subjectWhere = getFilterTokens(subject
				.getWhereClause());
		Set<FilterToken> learnedWhere = getFilterTokens(learned
				.getWhereClause());

		this.subjectColumnList = subject.getColumnList();
		this.learnedColumnList = learned.getColumnList();
		this.legalColumnsForNewFilters = createLegalColumns(learnedWhere);

		return checkWhereClause(subjectWhere, learnedWhere);
	}

	private Set<FilterToken> getFilterTokens(Set<? extends Token> set) {
		if (set == null) {
			return new HashSet<FilterToken>();
		}
		return copyFilterTokens(set);
	}

	/**
	 * Legal: column is in select list and not in where filter
	 */
	private Set<Token> createLegalColumns(Set<FilterToken> learnedWhere) {
		Set<Token> result = new HashSet<>();

		result.addAll(copyColumnNameTokens(learnedColumnList));
		result.addAll(copySelectStatementTokens(learnedColumnList));

		// remove the learned where columns/subqueries, because they are already
		// in use
		result.removeAll(copyRecursiveTokensOfType(learnedWhere,
				ColumnNameToken.class));
		result.removeAll(copyRecursiveTokensOfType(learnedWhere,
				SelectStatementToken.class));

		return result;
	}

	/**
	 * Checks the where clause. If a clause is not existent, an empty set will
	 * be passed.
	 */
	protected abstract LogEntry checkWhereClause(Set<FilterToken> subject,
			Set<FilterToken> learned);

}
