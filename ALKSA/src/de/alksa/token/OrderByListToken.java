package de.alksa.token;

import java.util.List;

public class OrderByListToken extends HierarchyToken {

	public OrderByListToken(List<? extends Token> orderByTokens) {
		setTokens(orderByTokens);
	}

}
