package de.alksa.classifier.impl;

import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.alksa.parser.Parser;
import de.alksa.parser.impl.ParserModule;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.impl.QueryImpl;
import de.alksa.token.Token;

public abstract class StateClassifierTest {

	private Parser parser;

	public StateClassifierTest() {
		Injector injector = Guice.createInjector(new ParserModule());
		parser = injector.getInstance(Parser.class);
	}

	protected Query createQuery(String sql, String database, String databaseUser) {
		Set<Token> tokens = parser.parse(sql);
		return new QueryImpl(tokens, sql, database, databaseUser);
	}
}
