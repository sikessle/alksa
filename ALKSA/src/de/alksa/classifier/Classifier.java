package de.alksa.classifier;

public interface Classifier {

	/**
	 * Checks or learns a query.
	 *
	 * @return True if the query was successfully checked. False if an error
	 *         occured.
	 */
	boolean accept(String sqlQuery, String database, String databaseUser);

	void setLearning(boolean learning);

	boolean isLearning();

}
