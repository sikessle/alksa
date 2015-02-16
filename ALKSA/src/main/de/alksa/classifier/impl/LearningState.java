package de.alksa.classifier.impl;

import java.util.Objects;

import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

class LearningState extends ClassifierState {

	private QueryStorage queryStorage;

	public LearningState(QueryStorage queryStorage) {
		Objects.requireNonNull(queryStorage);

		this.queryStorage = queryStorage;
	}

	@Override
	protected boolean acceptSingleQuery(Query query) {
		Objects.requireNonNull(query);

		queryStorage.write(query);

		return true;
	}

}
