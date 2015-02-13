package de.alksa.parser;

import java.util.Set;

import de.alksa.ALKSAInvalidQueryException;
import de.alksa.token.Token;

public interface Parser {

	/**
	 * Takes a SQL-query string and parses it.
	 * 
	 * @return The parsed query as a set of tokens which represent the query.
	 * @throws ALKSAInvalidQueryException
	 *             If the query was not unterstood by the parser.
	 */
	Set<Token> parse(String sql) throws ALKSAInvalidQueryException;

}
