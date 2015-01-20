package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.BinaryLogicalFilterToken;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.ConstantValueToken;
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
				Token actual = ((WhereClauseToken) token).getChildren().get(0);

				assertEquals(expected, actual);

				whereClauseTokenExists = true;
			}
		}

		if (!whereClauseTokenExists) {
			fail("No WhereClauseToken found");
		}
	}

	@Test
	public void testSimpleComparison() {
		// (a=2 AND b) OR NOT(c=a)
		String sql = "SELECT x FROM y WHERE a = 2 AND b OR NOT c = a";

		Token aEquals2 = new ComparisonFilterToken(new ColumnNameToken("a"),
				ComparisonFilterToken.Type.EQUAL, new ConstantValueToken(2));
		Token cEqualsA = new ComparisonFilterToken(new ColumnNameToken("c"),
				ComparisonFilterToken.Type.EQUAL, new ColumnNameToken("a"));
		
		Token andToken = new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.AND, aEquals2,
				new ColumnNameToken("b"));
		Token notToken = new UnaryLogicalFilterToken(
				UnaryLogicalFilterToken.Type.NOT, cEqualsA);

		Token expected = new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.OR, andToken, notToken);

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof WhereClauseToken) {
				Token actual = ((WhereClauseToken) token).getChildren().get(0);

				assertEquals(expected, actual);

			}
		}
	}
	
	@Test
	public void testComplicatedComparison() {
		// (a=2 AND b) OR NOT(c=a)
		fail("not implemented");
		String sql = "SELECT x FROM y WHERE a = 2 AND b OR NOT c = a";

		Token aEquals2 = new ComparisonFilterToken(new ColumnNameToken("a"),
				ComparisonFilterToken.Type.EQUAL, new ConstantValueToken(2));
		Token cEqualsA = new ComparisonFilterToken(new ColumnNameToken("c"),
				ComparisonFilterToken.Type.EQUAL, new ColumnNameToken("a"));
		
		Token andToken = new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.AND, aEquals2,
				new ColumnNameToken("b"));
		Token notToken = new UnaryLogicalFilterToken(
				UnaryLogicalFilterToken.Type.NOT, cEqualsA);

		Token expected = new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.OR, andToken, notToken);

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof WhereClauseToken) {
				Token actual = ((WhereClauseToken) token).getChildren().get(0);

				assertEquals(expected, actual);

			}
		}
	}

}
