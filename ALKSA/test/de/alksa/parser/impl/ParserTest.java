package de.alksa.parser.impl;

import java.util.Set;

import org.junit.Before;

import static org.junit.Assert.fail;

import de.alksa.ALKSAInvalidQueryException;
import de.alksa.token.Token;

public abstract class ParserTest {

	protected VisitorBasedParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new VisitorBasedParser();
	}

	protected Set<Token> exceptionSafeParse(String sql) {
		try {
			return parser.parse(sql);
		} catch (ALKSAInvalidQueryException e) {
			e.printStackTrace();
			fail("parsing failed: exception occured");
			return null;
		}
	}

}
