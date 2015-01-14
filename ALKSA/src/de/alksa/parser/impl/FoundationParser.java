package de.alksa.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.alksa.parser.Parser;
import de.alksa.token.Token;

public class FoundationParser implements Parser {
	
	private String sql;
	private List<Token> tokenizedQuery;

	@Override
	public List<Token> parse(String sql) {
		this.sql = sql;
		tokenizedQuery = new ArrayList<>();
		process();
		
		return tokenizedQuery;
	}

	private void process() {
		// TODO handle sql and fill tokenizedQuery
		// TODO if tokens remain, throw error (not yet supported exception, etc.)
		
	}

}
