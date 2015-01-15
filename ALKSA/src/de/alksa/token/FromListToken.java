package de.alksa.token;

import java.util.List;

public class FromListToken extends HierarchyToken {

	public FromListToken(List<? extends Token> tableTokens) {
		setTokens(tableTokens);
	}

}
