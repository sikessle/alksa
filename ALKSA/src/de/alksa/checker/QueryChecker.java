package de.alksa.checker;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import de.alksa.log.LogEntry;
import de.alksa.log.impl.AttackLogEntry;
import de.alksa.querystorage.Query;
import de.alksa.token.Token;

public abstract class QueryChecker {

	private QueryChecker next;
	private Query subject;

	public LogEntry checkSubjectAgainstLearned(Query subject, Query learned) {

		this.subject = subject;
		LogEntry log = check(subject.getQuery(), learned.getQuery());

		if (log == null && next != null) {
			return next.checkSubjectAgainstLearned(subject, learned);
		} else if (log != null) {
			return log;
		} else {
			return null;
		}
	}

	protected abstract LogEntry check(List<Token> subject, List<Token> learned);

	protected LogEntry createLogEntry(String violation) {
		String detailedViolation = this.getClass().getSimpleName() + ": "
				+ violation;

		return new AttackLogEntry(subject.getQueryString(),
				subject.getDatabase(), subject.getDatabaseUser(),
				detailedViolation, Instant.now());
	}

	public void appendMatcher(QueryChecker next) {
		Objects.requireNonNull(next);

		if (this.next == null) {
			this.next = next;
		} else {
			this.next.appendMatcher(next);
		}
	}

}
