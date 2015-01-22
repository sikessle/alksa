package de.alksa.parser;

import java.util.Set;

import de.alksa.token.Token;

public interface Parser {

	Set<Token> parse(String sql) throws RuntimeException;

}
