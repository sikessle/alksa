package de.alksa.token;

import java.util.List;

public class FunctionToken extends HierarchyToken {

	public FunctionToken(List<? extends Token> parameters) {
		setTokens(parameters);
	}

}
