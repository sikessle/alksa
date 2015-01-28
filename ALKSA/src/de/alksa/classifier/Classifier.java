package de.alksa.classifier;

import java.util.Set;

import de.alksa.ALKSAInvalidQueryException;
import de.alksa.log.LogEntry;

public interface Classifier {

	/**
	 * Checks or learns a query.
	 *
	 * @return True if the query was successfully checked. False if an error
	 *         occured.
	 */
	boolean accept(String sqlQuery, String database, String databaseUser)
			throws ALKSAInvalidQueryException;

	void setLearning(boolean learning);

	boolean isLearning();

	Set<LogEntry> getLogEntries();

}
