package de.alksa.parser.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.Visitable;
import com.foundationdb.sql.parser.Visitor;

import de.alksa.token.Token;

abstract class AbstractVisitor implements Visitor {

	private Set<Token> tokens = new HashSet<>();

	public Set<Token> getTokens() {
		return new HashSet<>(tokens);
	}

	protected void addToken(Token token) {
		if (token != null) {
			this.tokens.add(token);
		}
	}

	protected void addAllTokens(Set<Token> tokenList) {
		tokens.addAll(tokenList);
	}

	protected Set<Token> visitWithCombinedVisitor(Visitable node)
			throws StandardException {

		Objects.requireNonNull(node);

		AbstractVisitor visitor = new CombinedVisitor();
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
