package de.alksa.classifier.impl;

import java.util.Objects;

import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

class LearningClassifier implements ClassifierState {

	private QueryStorage queryStorage;

	public LearningClassifier(QueryStorage queryStorage) {
		Objects.requireNonNull(queryStorage);

		this.queryStorage = queryStorage;
	}

	@Override
	public boolean accept(Query query) {
		Objects.requireNonNull(query);

		return true;
	}

}
