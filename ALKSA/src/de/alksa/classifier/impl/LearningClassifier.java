package de.alksa.classifier.impl;

import java.util.Objects;

import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

class LearningClassifier extends ClassifierState {

	private QueryStorage queryStorage;

	public LearningClassifier(QueryStorage queryStorage) {
		Objects.requireNonNull(queryStorage);

		this.queryStorage = queryStorage;
	}

	@Override
	protected boolean acceptSingleSelectStatementQuery(Query query) {
		Objects.requireNonNull(query);

		queryStorage.write(query);

		return true;
	}

}
