package de.alksa.token;

import java.util.Set;

public class WhereClauseToken extends HierarchyToken {

	public WhereClauseToken(Set<? extends Token> filterTokens) {
		setTokens(filterTokens);
	}

}
