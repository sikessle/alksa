package de.alksa.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.StatementNode;

import de.alksa.parser.Parser;
import de.alksa.token.Token;

public class VisitorBasedParser implements Parser {

	private String sql;
	private List<Token> tokenizedQuery;
	private SQLParser sqlParser;
	private StatementNode stmt;

	public VisitorBasedParser() {
		tokenizedQuery = new ArrayList<>();
		sqlParser = new SQLParser();
	}

	@Override
	public List<Token> parse(String sql) {
		this.sql = sql;
		tokenizedQuery.clear();

		try {
			process();
		} catch (StandardException e) {
			throw new RuntimeException(e);
		}

		return tokenizedQuery;
	}

	private void process() throws StandardException {
		// TODO if tokens remain, throw error (not yet supported exception,
		// etc.)
		
		stmt = sqlParser.parseStatement(sql);
		
		processSelectQuery();
		// TODO process UNION, etc.
	}

	private void processSelectQuery() throws StandardException {
		SelectVisitor selectVisitor = new SelectVisitor();
//		stmt.treePrint();
		stmt.accept(selectVisitor);
		tokenizedQuery.addAll(selectVisitor.getTokens());
	}

}
