package de.alksa.parser.impl;

import java.util.HashSet;
import java.util.Set;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.StatementNode;
import com.google.inject.Singleton;

import de.alksa.parser.Parser;
import de.alksa.token.Token;

@Singleton
class VisitorBasedParser implements Parser {

	private String sql;
	private SQLParser sqlParser;
	private StatementNode stmt;

	public VisitorBasedParser() {
		sqlParser = new SQLParser();
	}

	@Override
	public Set<Token> parse(String sql) throws RuntimeException {
		this.sql = sql;
		Set<Token> tokenizedQuery = new HashSet<>();

		try {
			startParsing(tokenizedQuery);
		} catch (StandardException e) {
			throw new RuntimeException(e);
		}

		return tokenizedQuery;
	}

	private void startParsing(Set<Token> target) throws StandardException {
		stmt = sqlParser.parseStatement(sql);

		AbstractVisitor visitor = new RootVisitor();
		stmt.accept(visitor);
		target.addAll(visitor.getTokens());
	}

}
