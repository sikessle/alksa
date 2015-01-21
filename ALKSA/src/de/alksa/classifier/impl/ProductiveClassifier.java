package de.alksa.classifier.impl;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.log.Logger;
import de.alksa.log.impl.AttackLogEntry;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

class ProductiveClassifier implements ClassifierState {

	private QueryStorage queryStorage;
	private Logger logger;

	public ProductiveClassifier(QueryStorage queryStorage, Logger logger) {
		Objects.requireNonNull(queryStorage);
		Objects.requireNonNull(logger);

		this.queryStorage = queryStorage;
		this.logger = logger;
	}

	@Override
	public boolean accept(Query query) {
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
		Set<Query> learnedQueries = queryStorage.read();
		String violation = "<unknown>";

		// quick check for equal queries
		if (learnedQueries.contains(subject)) {
			return null;
		}

		// for (Query learned : queryStorage.read()) {
		// if () CREATE CHECKERS CLASSES IN LIST
		// }

		// ON FALSE: set logEntry!!!

		return new AttackLogEntry(subject.getQueryString(),
				subject.getDatabase(), subject.getDatabaseUser(), violation,
				Instant.now());
	}
}
