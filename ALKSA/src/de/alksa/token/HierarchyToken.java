package de.alksa.token;

import java.util.ArrayList;
import java.util.List;

public abstract class HierarchyToken extends Token {
	
	private List<Token> tokens;
	
	protected void setTokens(List<Token> tokens) {
		this.tokens = tokens == null ? new ArrayList<>() : tokens;
	}
	
	public List<Token> getChildren() {
		return tokens;
	}
}
