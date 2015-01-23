package de.alksa.checker;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.log.impl.AttackLogEntry;
import de.alksa.querystorage.Query;
import de.alksa.token.CalculationToken;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.FunctionToken;
import de.alksa.token.SelectStatementToken;
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

	public void appendMatcher(QueryChecker next) {
		Objects.requireNonNull(next);

		if (this.next == null) {
			this.next = next;
		} else {
			this.next.appendMatcher(next);
		}
	}

	/**
	 * It is expected that no implementations changes the parameters or its
	 * members.
	 */
	protected abstract LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned);

	protected LogEntry createLogEntry(String violation) {
		String detailedViolation = this.getClass().getSimpleName() + ": "
				+ violation;

		return new AttackLogEntry(subjectQueryData.getQueryString(),
				subjectQueryData.getDatabase(),
				subjectQueryData.getDatabaseUser(), detailedViolation,
				Instant.now());
	}

	protected boolean isSubset(Set<? extends Token> superSet,
			Set<? extends Token> subSet) {
		return superSet.containsAll(subSet);
	}

	protected boolean containsAsterisk(Set<? extends Token> tokens) {
		return tokens.contains(new ColumnNameToken("*"));
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

}
