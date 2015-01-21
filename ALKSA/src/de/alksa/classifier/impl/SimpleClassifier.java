package de.alksa.classifier.impl;

import java.util.List;
import java.util.Objects;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.alksa.classifier.Classifier;
import de.alksa.log.Logger;
import de.alksa.parser.Parser;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;
import de.alksa.querystorage.impl.QueryImpl;
import de.alksa.token.Token;

@Singleton
class SimpleClassifier implements Classifier {

	private ClassifierState state;
	private boolean learning;
	private final Parser parser;
	private final QueryStorage queryStorage;
	private final Logger logger;

	@Inject
	public SimpleClassifier(Parser parser, QueryStorage queryStorage,
			Logger logger) {

		Objects.requireNonNull(parser);
		Objects.requireNonNull(queryStorage);
		Objects.requireNonNull(logger);

		this.parser = parser;
		this.queryStorage = queryStorage;
		this.logger = logger;

		setLearning(false);
	}

	@Override
	public boolean accept(String sql, String database, String databaseUser) {
		if (sql == null || database == null || databaseUser == null) {
			return false;
		}

		try {
			Query query = createQuery(sql, database, databaseUser);
			return state.accept(query);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return false;
		}

	}

	private Query createQuery(String sql, String database, String databaseUser) {
		List<Token> tokens = parser.parse(sql);
		return new QueryImpl(tokens, sql, database, databaseUser);
	}

	@Override
	public boolean isLearning() {
		return learning;
	}

	@Override
	public void setLearning(boolean learning) {
		this.learning = learning;

		if (isLearning()) {
			state = new LearningClassifier(queryStorage);
		} else {
			state = new ProductiveClassifier(queryStorage, logger);
		}
	}

}
