package de.alksa.checker;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.log.impl.AttackLogEntry;
import de.alksa.querystorage.Query;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;

public abstract class QueryChecker {

	private QueryChecker next;
	private Query subject;

	public LogEntry checkSubjectAgainstLearned(Query subject, Query learned) {

		this.subject = subject;
		LogEntry log = check(getSelectStatementToken(subject),
				getSelectStatementToken(learned));

		if (log == null && next != null) {
			return next.checkSubjectAgainstLearned(subject, learned);
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

	private SelectStatementToken getSelectStatementToken(Query query) {
		for (Token token : query.getQuery()) {
			if (token instanceof SelectStatementToken) {
				return (SelectStatementToken) token;
			}
		}
		throw new IllegalStateException("no SelectStatementToken found");
	}

	protected abstract LogEntry check(SelectStatementToken subject,
			SelectStatementToken learned);

	protected LogEntry createLogEntry(String violation) {
		String detailedViolation = this.getClass().getSimpleName() + ": "
				+ violation;

		return new AttackLogEntry(subject.getQueryString(),
				subject.getDatabase(), subject.getDatabaseUser(),
				detailedViolation, Instant.now());
	}

	@SuppressWarnings("unchecked")
	protected <T> T getFirstTokenOfType(Set<? extends Token> tokens,
			Class<T> clazz) {

		for (Token token : tokens) {
			if (clazz.isInstance(token)) {
				return (T) token;
			}
		}

		return null;
	}

}
