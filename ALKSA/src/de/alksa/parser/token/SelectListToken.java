package de.alksa.parser.token;

import java.util.ArrayList;
import java.util.List;

public class SelectListToken extends Token implements HierarchyToken {

	private List<Token> tokens;

	public SelectListToken(List<Token> tokens) {
		this.tokens = tokens == null ? new ArrayList<>() : tokens;
	}

	public List<Token> getChildren() {
		return tokens;
	}

}
