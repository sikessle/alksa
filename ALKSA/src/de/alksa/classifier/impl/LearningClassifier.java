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
		Set<Query> selectStatements = new HashSet<>();
		Query select = null;

		// TODO currently we save the original queryString with UNION etc. It
		// would be better to save just the appropriate part. Limitation by
		// parser.
		for (Token token : multiQuery.getQuery()) {
			select = separateTopLevelSelectStatements(token, multiQuery);
			if (select != null) {
				selectStatements.add(select);
			}
		}

		for (Query query : selectStatements) {
			queryStorage.write(query);
		}

		return true;
	}

	private Query separateTopLevelSelectStatements(Token subject,
			Query multiQuery) {
		if (subject instanceof SelectStatementToken) {
			return new QueryImpl(new HashSet<>(Arrays.asList(subject)),
					multiQuery.getQueryString(), multiQuery.getDatabase(),
					multiQuery.getDatabaseUser());
		}
		return null;
	}
}
