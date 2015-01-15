package de.alksa.token;

import java.util.List;

public class SelectColumnListToken extends HierarchyToken {

	public SelectColumnListToken(List<? extends Token> columnTokens) {
		setTokens(columnTokens);
	}

}
