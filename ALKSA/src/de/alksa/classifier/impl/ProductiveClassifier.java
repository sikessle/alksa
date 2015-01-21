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
	private LogEntry latestLogEntry;

	public ProductiveClassifier(QueryStorage queryStorage, Logger logger) {
		Objects.requireNonNull(queryStorage);
		Objects.requireNonNull(logger);

		this.queryStorage = queryStorage;
		this.logger = logger;
	}

	@Override
	public boolean accept(Query query) {
		Objects.requireNonNull(query);

		if (!isQueryAllowed(query)) {
			logger.write(latestLogEntry);
			return false;
		}

		return true;
	}

	private boolean isQueryAllowed(Query test) {
		Set<Query> learnedQueries = queryStorage.read();

		// quick check for equal queries
		if (learnedQueries.contains(test)) {
			return true;
		}

		// for (Query learned : queryStorage.read()) {
		// if ()
		// }

		// ON FALSE: set logEntry!!!

		latestLogEntry = new AttackLogEntry(test.getQueryString(),
				test.getDatabase(), test.getDatabaseUser(), "CAUSE",
				Instant.now());

		return false;
	}
}
