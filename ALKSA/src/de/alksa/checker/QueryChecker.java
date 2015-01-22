package de.alksa.checker;

import java.time.Instant;
import java.util.Objects;

import de.alksa.log.LogEntry;
import de.alksa.log.impl.AttackLogEntry;
import de.alksa.querystorage.Query;
import de.alksa.token.SelectStatementToken;

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

}
