package de.alksa.token;

import java.util.List;

public class SelectStatementToken extends HierarchyToken {

	public SelectStatementToken(List<? extends Token> tokens) {
		setTokens(tokens);
	}

	@Override
	public String toString() {
		return "SELECT [" + super.toString() + "]";
	}

}
