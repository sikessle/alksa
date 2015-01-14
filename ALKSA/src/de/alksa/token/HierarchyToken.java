package de.alksa.token;

import java.util.List;

public abstract class HierarchyToken extends Token {
	
	protected List<Token> tokens;
	
	public List<Token> getChildren() {
		return tokens;
	}
}
