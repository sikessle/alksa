package de.alksa.classifier.impl;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.alksa.parser.Parser;
import de.alksa.parser.impl.ParserModule;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.impl.SingleSelectQuery;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;
import de.alksa.util.TypeUtil;

public abstract class StateClassifierTest {

	private Parser parser;

	public StateClassifierTest() {
		Injector injector = Guice.createInjector(new ParserModule());
		parser = injector.getInstance(Parser.class);
	}

	protected Set<Query> createQueries(String sql, String database,
			String databaseUser) {
		Set<Query> queries = new HashSet<>();

		Set<Token> tokens = parser.parse(sql);

		Set<SelectStatementToken> selectStatements = TypeUtil
				.getAllTokensOfType(tokens, SelectStatementToken.class);

		for (SelectStatementToken select : selectStatements) {
			queries.add(new SingleSelectQuery(select, sql, database,
					databaseUser));
		}

		return queries;
	}
}
