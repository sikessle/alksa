package de.alksa.parser;

import java.util.List;

import de.alksa.parser.token.Token;

public interface Parser {

	List<Token> parse(String sql);
	
}
