package de.alksa.checker;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.log.impl.LogEntryImpl;
import de.alksa.querystorage.Query;
import de.alksa.token.CalculationToken;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.FilterToken;
import de.alksa.token.FunctionToken;
import de.alksa.token.HierarchyToken;
import de.alksa.token.JoinToken;
import de.alksa.token.LogicalFilterToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.TableNameToken;
import de.alksa.token.Token;
import de.alksa.util.TypeUtil;

public abstract class QueryChecker {

	private QueryChecker next;
	private Query subjectQueryData;

	public LogEntry checkSubjectAgainstLearned(SelectStatementToken subject,
			Query subjectQueryData, SelectStatementToken learned) {

		this.subjectQueryData = subjectQueryData;

		LogEntry log = check(subject, learned);

		if (log == null && next != null) {
			return next.checkSubjectAgainstLearned(subject, subjectQueryData,
					learned);
		} else if (log != null) {
			return log;
		} else {
			return null;
		}
	}

	/**
	 * Allows to chain checkers like a list.
	 */
	public void appendChecker(QueryChecker next) {
		Objects.requireNonNull(next);

		if (this.next == null) {
			this.next = next;
		} else {
			this.next.appendChecker(next);
		}
	}

	/**
	 * It is expected that no implementations changes the parameters or its
	 * members.
	 *
	 * @param subject
	 *            The subject to check
	 * @param learned
	 *            The learned query to compare against
	 * @return Returns a LogEntry if an the query is not allowed, otherwise null
	 *         is returned.
	 */
	protected abstract LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned);

	/**
	 * Creates a LogEntry with the current subject-query, database and
	 * database-user.
	 * 
	 * @param violation
	 *            The cause, why the query is not allowed.
	 */
	protected LogEntry createLogEntry(String violation) {
		String detailedViolation = this.getClass().getSimpleName() + ": "
				+ violation;

		return new LogEntryImpl(subjectQueryData.getQueryString(),
				subjectQueryData.getDatabase(),
				subjectQueryData.getDatabaseUser(), detailedViolation,
				Instant.now());
	}

	protected boolean isSubset(Set<? extends Token> superSet,
			Set<? extends Token> subSet) {
		return superSet.containsAll(subSet);
	}

	@SuppressWarnings("unchecked")
	protected <T> Set<T> copyRecursiveTokensOfType(Token token, Class<T> type) {
		Set<T> result = new HashSet<>();

		if (type.isInstance(token)) {
			result.add((T) token);
		}

		if (token instanceof HierarchyToken) {
			HierarchyToken hToken = (HierarchyToken) token;
			for (Token child : hToken.getChildren()) {
				if (type.isInstance(child)) {
					result.add((T) child);
				} else if (child instanceof HierarchyToken) {
					result.addAll(copyRecursiveTokensOfType(child, type));
				}
			}
		}

		return result;
	}

	protected <T> Set<T> copyRecursiveTokensOfType(Set<? extends Token> tokens,
			Class<T> type) {
		Set<T> result = new HashSet<>();

		for (Token token : tokens) {
			result.addAll(copyRecursiveTokensOfType(token, type));
		}

		return result;
	}

	protected boolean containsAsterisk(Set<? extends Token> tokens) {
		return tokens.contains(new ColumnNameToken("*"));
	}

	protected Set<HierarchyToken> copyHierarchyTokens(
			Set<? extends Token> tokens) {
		return TypeUtil.getAllTokensOfType(tokens, HierarchyToken.class);
	}

	protected Set<ColumnNameToken> copyColumnNameTokens(
			Set<? extends Token> tokens) {
		return TypeUtil.getAllTokensOfType(tokens, ColumnNameToken.class);
	}

	protected Set<FunctionToken> copyFunctionTokens(Set<? extends Token> tokens) {
		return TypeUtil.getAllTokensOfType(tokens, FunctionToken.class);
	}

	protected Set<CalculationToken> copyCalculationTokens(
			Set<? extends Token> tokens) {
		return TypeUtil.getAllTokensOfType(tokens, CalculationToken.class);
	}

	protected Set<SelectStatementToken> copySelectStatementTokens(
			Set<? extends Token> tokens) {
		return TypeUtil.getAllTokensOfType(tokens, SelectStatementToken.class);
	}

	protected Set<TableNameToken> copyTableNameTokens(
			Set<? extends Token> tokens) {
		return TypeUtil.getAllTokensOfType(tokens, TableNameToken.class);
	}

	protected Set<JoinToken> copyJoinTokens(Set<? extends Token> tokens) {
		return TypeUtil.getAllTokensOfType(tokens, JoinToken.class);
	}

	protected Set<ComparisonFilterToken> copyComparisonFilterTokens(
			Set<? extends Token> tokens) {
		return TypeUtil.getAllTokensOfType(tokens, ComparisonFilterToken.class);
	}

	protected Set<FilterToken> copyFilterTokens(Set<? extends Token> tokens) {
		return TypeUtil.getAllTokensOfType(tokens, FilterToken.class);
	}

	protected Set<LogicalFilterToken> copyLogicalFilterTokens(
			Set<? extends Token> tokens) {
		return TypeUtil.getAllTokensOfType(tokens, LogicalFilterToken.class);
	}

}
