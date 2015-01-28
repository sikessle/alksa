package de.alksa.parser.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.alksa.token.BinaryLogicalFilterToken;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.ConstantValueToken;
import de.alksa.token.FunctionToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;
import de.alksa.token.UnaryLogicalFilterToken;

import static org.junit.Assert.assertEquals;

/**
 * Checks filters in WHERE Statements
 */
public class ParserFilterClauseTest extends ParserTest {

	@Test
	public void testLogicalOperators() {
		// (a AND b) OR NOT(c)
		String sql = "SELECT x FROM y WHERE a AND b OR NOT c";
		Token andToken = new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.AND, new ColumnNameToken("a"),
				new ColumnNameToken("b"));
		Token notToken = new UnaryLogicalFilterToken(
				UnaryLogicalFilterToken.Type.NOT, new ColumnNameToken("c"));

		Set<? extends Token> expected = new HashSet<>(
				Arrays.asList(new BinaryLogicalFilterToken(
						BinaryLogicalFilterToken.Type.OR, andToken, notToken)));

		Set<Token> parsedTokens = exceptionSafeParse(sql);

		Set<? extends Token> actual = ((SelectStatementToken) parsedTokens
				.iterator().next()).getWhereClause();

		assertEquals(expected, actual);
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

		Set<? extends Token> expected = new HashSet<>(
				Arrays.asList(new BinaryLogicalFilterToken(
						BinaryLogicalFilterToken.Type.OR, andToken, notToken)));

		Set<Token> parsedTokens = exceptionSafeParse(sql);

		Set<? extends Token> actual = ((SelectStatementToken) parsedTokens
				.iterator().next()).getWhereClause();

		assertEquals(expected, actual);
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
		Set<Token> parameters = new HashSet<>();
		parameters.add(new ConstantValueToken(2));
		Token cEqualAbs2 = new ComparisonFilterToken(new ColumnNameToken("c"),
				ComparisonFilterToken.Type.EQUAL, new FunctionToken("ABS",
						parameters));

		Token andToken = new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.AND, bLess3, cEqualAbs2);

		Set<? extends Token> expected = new HashSet<>(
				Arrays.asList(new BinaryLogicalFilterToken(
						BinaryLogicalFilterToken.Type.OR, aGreaterEqual2,
						andToken)));

		Set<Token> parsedTokens = exceptionSafeParse(sql);

		Set<? extends Token> actual = ((SelectStatementToken) parsedTokens
				.iterator().next()).getWhereClause();

		assertEquals(expected, actual);
	}

	@Test
	public void testHavingClause() {
		// AVG(col1) > 10
		String sql = "SELECT AVG(col1) FROM y GROUP BY col1 HAVING AVG(col1) > 10";

		Set<? extends Token> expected = new HashSet<>(
				Arrays.asList(new ComparisonFilterToken(new FunctionToken(
						"AVG", new HashSet<>(Arrays.asList(new ColumnNameToken(
								"col1")))), ComparisonFilterToken.Type.GREATER,
								new ConstantValueToken(10))));

		Set<Token> parsedTokens = exceptionSafeParse(sql);

		Set<? extends Token> actual = ((SelectStatementToken) parsedTokens
				.iterator().next()).getHavingClause();

		assertEquals(expected, actual);
	}
}
