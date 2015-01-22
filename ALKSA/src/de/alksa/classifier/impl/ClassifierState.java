package de.alksa.classifier.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.alksa.querystorage.Query;
import de.alksa.querystorage.impl.QueryImpl;
import de.alksa.token.SelectStatementToken;
import de.alksa.util.TypeUtil;

abstract class ClassifierState {

	public final boolean accept(Query query) {

		for (Query singleSelectQuery : splitIntoSingleSelectStatementQueries(query)) {
			if (!acceptSingleSelectStatementQuery(singleSelectQuery)) {
				return false;
			}
		}

		return true;
	}

	protected abstract boolean acceptSingleSelectStatementQuery(Query query);

	private Set<Query> splitIntoSingleSelectStatementQueries(Query query) {

		Set<SelectStatementToken> selectTokens = TypeUtil
				.getAllTokensOfType(query.getQuery(),
						SelectStatementToken.class);

		Set<Query> selectQueries = new HashSet<>();

		for (SelectStatementToken select : selectTokens) {
			selectQueries.add(createQuery(select, query));
		}

		return selectQueries;
	}

	private Query createQuery(SelectStatementToken token, Query parent) {
		return new QueryImpl(new HashSet<>(Arrays.asList(token)),
				parent.getQueryString(), parent.getDatabase(),
				parent.getDatabaseUser());
	}

}
