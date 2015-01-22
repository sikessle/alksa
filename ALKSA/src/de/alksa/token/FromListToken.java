package de.alksa.token;

import java.util.Set;

public class FromListToken extends HierarchyToken {

	public FromListToken(Set<? extends Token> tableTokens) {
		setTokens(tableTokens);
	}

}
