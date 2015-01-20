package de.alksa.token;

import java.util.List;

public class WhereClauseToken extends HierarchyToken {

	public WhereClauseToken(List<? extends Token> filterTokens) {
		setTokens(filterTokens);
	}

}
