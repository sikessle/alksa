package de.alksa.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.Visitable;
import com.foundationdb.sql.parser.Visitor;

import de.alksa.token.Token;

abstract class AbstractVisitor implements Visitor {

	private List<Token> tokens = new ArrayList<>();

	public List<Token> getTokens() {
		return tokens;
	}

	protected void addToken(Token token) {
		if (token != null) {
			this.tokens.add(token);
		}
	}

	protected void addAllTokens(List<Token> tokenList) {
		for (Token t : tokenList) {
			this.tokens.add(t);
		}
	}

	protected List<Token> getRecursiveAllTokensOfNode(Visitable node)
			throws StandardException {

		Objects.requireNonNull(node);

		AbstractVisitor visitor = new MasterVisitor();
		node.accept(visitor);

		return visitor.getTokens();
	}

	@Override
	public boolean skipChildren(Visitable node) throws StandardException {
		return true;
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
