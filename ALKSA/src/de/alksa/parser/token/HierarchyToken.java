package de.alksa.parser.token;

import java.util.List;

public abstract class HierarchyToken extends Token {
	
	public abstract List<? extends Token> getChildren();
}
