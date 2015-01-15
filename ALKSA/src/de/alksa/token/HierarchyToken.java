package de.alksa.token;

import java.util.ArrayList;
import java.util.List;

public abstract class HierarchyToken extends Token {
	
	private List<? extends Token> tokens;
	
	protected void setTokens(List<? extends Token> tokens) {
		this.tokens = tokens == null ? new ArrayList<>() : tokens;
	}
	
	public List<? extends Token> getChildren() {
		return tokens;
	}
}
