package de.alksa.token;

import java.util.Set;

public class HavingClauseToken extends HierarchyToken {

	public HavingClauseToken(Set<? extends Token> filterTokens) {
		setTokens(filterTokens);
	}

	@Override
	public String toString() {
		return " HAVING " + super.toString();
	}

}
