package de.alksa.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.Visitable;
import com.foundationdb.sql.parser.Visitor;

import de.alksa.token.Token;

public abstract class AbstractVisitor implements Visitor {
	
	private List<Token> tokens = new ArrayList<>();
	
	public List<Token> getTokens() {
		return tokens;
	}
	
	protected void addToken(Token token) {
		tokens.add(token);
	}
	
	@Override
	public boolean skipChildren(Visitable node) throws StandardException {
		return false;
	}
	
	@Override
	public boolean stopTraversal() {
		return false;
	}
	
	@Override
	public boolean visitChildrenFirst(Visitable node) {
		return false;
	}
	
}
