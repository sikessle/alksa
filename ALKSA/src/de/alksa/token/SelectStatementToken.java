package de.alksa.token;

import java.util.Set;

public class SelectStatementToken extends HierarchyToken {

	public SelectStatementToken(Set<? extends Token> tokens) {
		setTokens(tokens);
	}

	@Override
	public String toString() {
		return "SELECT [" + super.toString() + "]";
	}

}
