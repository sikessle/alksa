package de.alksa.classifier.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.alksa.ALKSAInvalidQueryException;
import de.alksa.checker.QueryChecker;
import de.alksa.classifier.Classifier;
import de.alksa.log.LogEntry;
import de.alksa.log.Logger;
import de.alksa.parser.Parser;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;
import de.alksa.querystorage.impl.SingleSelectQuery;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;
import de.alksa.util.TypeUtil;

@Singleton
class CheckerBasedClassifier implements Classifier {

	private ClassifierState state;
	private boolean learning;
	private final Parser parser;
	private final QueryStorage queryStorage;
	private final Logger logger;
	private QueryChecker masterChecker;

	@Inject
	public CheckerBasedClassifier(Set<QueryChecker> queryCheckers,
			Parser parser, QueryStorage queryStorage, Logger logger) {

		Objects.requireNonNull(queryCheckers);
		Objects.requireNonNull(parser);
		Objects.requireNonNull(queryStorage);
		Objects.requireNonNull(logger);

		this.parser = parser;
		this.queryStorage = queryStorage;
		this.logger = logger;

		masterChecker = createDummyChecker();

		for (QueryChecker checker : queryCheckers) {
			masterChecker.appendChecker(checker);
		}

		setLearning(false);
	}

	private QueryChecker createDummyChecker() {
		return new QueryChecker() {

			@Override
			protected LogEntry check(SelectStatementToken subject,
					SelectStatementToken learned) {
				return null;
			}
		};
	}

	@Override
	public boolean accept(String sql, String database, String databaseUser)
			throws ALKSAInvalidQueryException {
		if (sql == null || database == null || databaseUser == null) {
			throw new ALKSAInvalidQueryException("parameters are null");
		}

		Set<Query> queries = createQueries(sql, database, databaseUser);
		return state.accept(queries);

	}

	private Set<Query> createQueries(String sql, String database,
			String databaseUser) throws ALKSAInvalidQueryException {
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

	@Override
	public boolean isLearning() {
		return learning;
	}

	@Override
	public void setLearning(boolean learning) {
		this.learning = learning;

		if (isLearning()) {
			state = new LearningState(queryStorage);
		} else {
			state = new ProductiveState(masterChecker, queryStorage, logger);
		}
	}

	@Override
	public Set<LogEntry> getLogEntries() {
		return logger.read();
	}

	@Override
	public Set<Query> getLearnedQueries() {
		return queryStorage.read();
	}

}
