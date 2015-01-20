package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.BinaryLogicalFilterToken;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.Token;
import de.alksa.token.UnaryLogicalFilterToken;
import de.alksa.token.WhereClauseToken;

/**
 * Checks filters in WHERE Statements
 */
public class ParserFilterClauseTest {

	private VisitorBasedParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new VisitorBasedParser();
	}

	@Test
	public void testLogicalOperators() {
		// (a AND b) OR NOT(c)
		String sql = "SELECT x FROM y WHERE a AND b OR NOT c";
		List<? extends Token> actual;
		boolean whereClauseTokenExists = false;

		Token andToken = new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.AND, new ColumnNameToken("a"),
				new ColumnNameToken("b"));
		Token notToken = new UnaryLogicalFilterToken(
				UnaryLogicalFilterToken.Type.NOT, new ColumnNameToken("c"));

		Token expected = new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.OR, andToken, notToken);

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof WhereClauseToken) {
				actual = ((WhereClauseToken) token).getChildren();

				assertEquals(1, actual.size());
				assertTrue(actual.contains(expected));

				whereClauseTokenExists = true;
			}
		}

		if (!whereClauseTokenExists) {
			fail("No WhereClauseToken found");
		}
	}

}
