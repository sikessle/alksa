package de.alksa.checker;

import java.time.Instant;
import java.util.Objects;

import de.alksa.log.LogEntry;
import de.alksa.log.impl.AttackLogEntry;
import de.alksa.querystorage.Query;

public abstract class QueryChecker {

	private QueryChecker next;

	public LogEntry checkSubjectAgainstLearned(Query subject, Query learned) {
		LogEntry log = check(subject, learned);

		if (log == null && next != null) {
			return next.checkSubjectAgainstLearned(subject, learned);
		} else if (log != null) {
			return log;
		} else {
			return null;
		}
	}

	protected abstract LogEntry check(Query subject, Query learned);

	protected LogEntry createLogEntry(Query subject, String violation) {
		return new AttackLogEntry(subject.getQueryString(),
				subject.getDatabase(), subject.getDatabaseUser(), violation,
				Instant.now());
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
