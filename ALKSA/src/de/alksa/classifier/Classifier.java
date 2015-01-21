package de.alksa.classifier;

import de.alksa.querystorage.Query;

public interface Classifier {

	/**
	 * Checks or learns a query.
	 *
	 * @return True if the query was successfully checked. False if an error
	 *         occured.
	 */
	boolean accept(Query query);

	void setLearning(boolean learning);

	boolean isLearning();

}
