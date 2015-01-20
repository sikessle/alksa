package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.BinaryLogicalFilterToken;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.ConstantValueToken;
import de.alksa.token.FunctionToken;
import de.alksa.token.HavingClauseToken;
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
		// (a >= 2) OR (b < 3 AND c = ABS(2))
		String sql = "SELECT x FROM y WHERE a >= 2 OR b < 3 AND c = ABS(2)";

		Token aGreaterEqual2 = new ComparisonFilterToken(new ColumnNameToken(
				"a"), ComparisonFilterToken.Type.GREATER_EQUAL,
				new ConstantValueToken(2));
		Token bLess3 = new ComparisonFilterToken(new ColumnNameToken("b"),
				ComparisonFilterToken.Type.LESS, new ConstantValueToken(3));
		List<Token> parameters = new ArrayList<>();
		parameters.add(new ConstantValueToken(2));
		Token cEqualAbs2 = new ComparisonFilterToken(new ColumnNameToken("c"),
				ComparisonFilterToken.Type.EQUAL, new FunctionToken("ABS",
						parameters));

		Token andToken = new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.AND, bLess3, cEqualAbs2);

		Token expected = new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.OR, aGreaterEqual2, andToken);

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
	public void testHavingClause() {
		// AVG(col1) > 10
		String sql = "SELECT AVG(col1) FROM y GROUP BY col1 HAVING AVG(col1) > 10";
		boolean havingClauseTokenExists = false;

		Token expected = new ComparisonFilterToken(new FunctionToken("AVG",
				Arrays.asList(new ColumnNameToken("col1"))),
				ComparisonFilterToken.Type.GREATER, new ConstantValueToken(10));

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof HavingClauseToken) {
				Token actual = ((HavingClauseToken) token).getChildren().get(0);

				assertEquals(expected, actual);

				havingClauseTokenExists = true;
			}
		}

		if (!havingClauseTokenExists) {
			fail("No HavingClauseToken found");
		}
	}
}
