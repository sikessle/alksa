package de.alksa.token;

import java.util.List;

public class HavingClauseToken extends HierarchyToken {

	public HavingClauseToken(List<? extends Token> filterTokens) {
		setTokens(filterTokens);
	}

	@Override
	public String toString() {
		return " HAVING " + super.toString();
	}

}
