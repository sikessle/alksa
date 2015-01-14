package de.alksa.parser;

import java.util.List;

public interface Parser {

	List<Token> parse(String sql);
	
}
