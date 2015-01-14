package de.alksa.token;

import java.util.ArrayList;
import java.util.List;

public class SelectListToken extends HierarchyToken {

	public SelectListToken(List<Token> tokens) {
		this.tokens = tokens == null ? new ArrayList<>() : tokens;
	}

}
