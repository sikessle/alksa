package de.alksa.classifier.impl;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

import de.alksa.checker.QueryChecker;
import de.alksa.log.LogEntry;
import de.alksa.log.Logger;
import de.alksa.log.impl.AttackLogEntry;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;
import de.alksa.token.SelectStatementToken;

class ProductiveState extends ClassifierState {

	private QueryStorage queryStorage;
	private Logger logger;
	private QueryChecker masterChecker;

	public ProductiveState(Set<QueryChecker> queryCheckers,
			QueryStorage queryStorage, Logger logger) {
		Objects.requireNonNull(queryCheckers);
		Objects.requireNonNull(queryStorage);
		Objects.requireNonNull(logger);

		masterChecker = createDummyChecker();

		for (QueryChecker checker : queryCheckers) {
			masterChecker.appendMatcher(checker);
		}

		this.queryStorage = queryStorage;
		this.logger = logger;
	}

	@Override
	protected boolean acceptSingleQuery(Query query) {
		Objects.requireNonNull(query);

		LogEntry log = checkQuery(query);

		if (log != null) {
			logger.write(log);
			return false;
		}

		return true;
	}

	/**
	 * Checks the subject against the learned queries and decides, if it is
	 * allowed or not.
	 *
	 * @return null if the query is allowed. If it is disallowed a LogEntry is
	 *         created and returned.
	 */
	private LogEntry checkQuery(Query subject) {

		System.out.println(subject.getSelectStatement());

		Set<Query> learnedQueries = queryStorage.read();
		LogEntry log = null;

		// can't compare to non-existing queries
		if (learnedQueries.isEmpty()) {
			return new AttackLogEntry(subject.getQueryString(),
					subject.getDatabase(), subject.getDatabaseUser(),
					"no matching query found", Instant.now());
		}

		// quick check for equal queries
		if (learnedQueries.contains(subject)) {
			return null;
		}

		SelectStatementToken subjectSelect = subject.getSelectStatement();
		SelectStatementToken learnedSelect;

		for (Query learned : learnedQueries) {
			// TODO better performance: Use Map structure for storing the
			// queries
			if (isNotSameDatabaseAndUser(subject, learned)) {
				continue;
			}

			learnedSelect = learned.getSelectStatement();

			log = masterChecker.checkSubjectAgainstLearned(subjectSelect,
					subject, learnedSelect);

			if (log == null) {
				// subject has found one learned query in database which is
				// similar.
				break;
			}
		}

		return log;
	}

	private boolean isNotSameDatabaseAndUser(Query subject, Query learned) {
		return !(subject.getDatabase().equals(learned.getDatabase()) && subject
				.getDatabaseUser().equals(learned.getDatabaseUser()));
	}

	private QueryChecker createDummyChecker() {
		return new QueryChecker() {

			@Override
			protected LogEntry check(SelectStatementToken subject,
					SelectStatementToken learned) {
				return null;
			}
		};
	}
}
