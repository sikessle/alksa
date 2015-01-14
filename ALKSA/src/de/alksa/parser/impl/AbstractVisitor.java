package de.alksa.parser.impl;

import java.util.List;

import com.foundationdb.sql.parser.Visitor;

import de.alksa.token.Token;

public abstract class AbstractVisitor implements Visitor {
	
	private List<Token> tokens;
	
	public List<Token> getTokens() {
		return tokens;
	}
	
	protected void addToken(Token token) {
		tokens.add(token);
	}
	
}
