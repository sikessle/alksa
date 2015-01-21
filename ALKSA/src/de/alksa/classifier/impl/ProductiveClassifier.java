package de.alksa.classifier.impl;

import java.util.Objects;

import de.alksa.log.Logger;
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

		return false;
	}

}
