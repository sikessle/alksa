package de.alksa.parser.token;

import java.util.List;

public interface HierarchyToken {
	
	List<? extends Token> getChildren();
}
