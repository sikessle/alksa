package de.alksa.classifier.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;
import de.alksa.querystorage.impl.QueryImpl;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;

class LearningClassifier implements ClassifierState {

	private QueryStorage queryStorage;

	public LearningClassifier(QueryStorage queryStorage) {
		Objects.requireNonNull(queryStorage);

		this.queryStorage = queryStorage;
	}

	@Override
	public boolean accept(Query multiQuery) {
		Objects.requireNonNull(multiQuery);
		Set<Query> queries = new HashSet<>();

		for (Token token : multiQuery.getQuery()) {
			if (token instanceof SelectStatementToken) {
				queries.add(new QueryImpl(Arrays.asList(token), multiQuery
						.getQueryString(), multiQuery.getDatabase(), multiQuery
						.getDatabaseUser()));
			}
		}

		for (Query query : queries) {
			queryStorage.write(query);
		}

		return true;
	}
}
