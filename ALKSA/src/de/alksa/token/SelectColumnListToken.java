package de.alksa.token;

import java.util.Set;

public class SelectColumnListToken extends HierarchyToken {

	public SelectColumnListToken(Set<? extends Token> columnTokens) {
		setTokens(columnTokens);
	}

}
