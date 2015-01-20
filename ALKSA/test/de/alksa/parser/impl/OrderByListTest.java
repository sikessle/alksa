package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.ColumnNameToken;
import de.alksa.token.OrderByListToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;

/**
 * Checks ORDER BY
 */
public class OrderByListTest {

	private VisitorBasedParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new VisitorBasedParser();
	}

	@Test
	public void testOrderBy() {
		// ORDER BY col1 DESC
		String sql = "SELECT x FROM y ORDER BY col1 DESC";
		boolean orderByListTokenExists = false;

		Token expected = new ColumnNameToken("col1");

		List<Token> parsedTokens = parser.parse(sql);

		List<? extends Token> tokens = ((SelectStatementToken) parsedTokens
				.get(0)).getChildren();

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof OrderByListToken) {
				Token actual = ((OrderByListToken) token).getChildren().get(0);

				assertEquals(expected, actual);

				orderByListTokenExists = true;
			}
		}

		if (!orderByListTokenExists) {
			fail("No OrderByListToken found");
		}
	}
}
