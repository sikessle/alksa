package de.alksa.parser.impl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.CalculationToken;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.FunctionToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserSelectColumnListTest {

	private VisitorBasedParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new VisitorBasedParser();
	}

	@Test(expected = RuntimeException.class)
	public void testParseException() {
		parser.parse(";Not Valid SQL;");
	}

	@Test
	public void testColumnNameWithAlias() {
		// hiddenCol and col3 should not come up in the select column list
		String sql = "SELECT users.col1 AS c1, col2, ABS(col3) FROM users WHERE hiddenCol = 'a'";
		Set<ColumnNameToken> expected = new HashSet<>();

		expected.add(new ColumnNameToken("users.col1"));
		expected.add(new ColumnNameToken("col2"));

		Set<Token> parsedTokens = parser.parse(sql);

		Set<? extends Token> actual = ((SelectStatementToken) parsedTokens
				.iterator().next()).getColumnList();

		// minus 1 because of the ABS(col3) column
		assertEquals(expected.size(), actual.size() - 1);
		assertTrue(actual.containsAll(expected));
	}

	@Test
	public void testColumnNameWithAsterisk() {
		String sql = "SELECT * FROM users";
		Set<ColumnNameToken> expected = new HashSet<>();

		expected.add(new ColumnNameToken("*"));

		Set<Token> parsedTokens = parser.parse(sql);

		Set<? extends Token> actual = ((SelectStatementToken) parsedTokens
				.iterator().next()).getColumnList();

		assertEquals(expected.size(), actual.size());
		assertTrue(actual.containsAll(expected));
	}

	@Test
	public void testColumnListWithFunctions() {
		String sql = "SELECT ABS(col1), AVG(col2) FROM users";
		Set<Token> functionParametersABS = new HashSet<>();
		Set<Token> functionParametersAVG = new HashSet<>();
		Set<Token> expected = new HashSet<>();

		// ABS(col1)
		functionParametersABS.add(new ColumnNameToken("col1"));
		// AVG(col2)
		functionParametersAVG.add(new ColumnNameToken("col2"));

		expected.add(new FunctionToken("ABS", functionParametersABS));
		expected.add(new FunctionToken("AVG", functionParametersAVG));

		Set<Token> parsedTokens = parser.parse(sql);

		Set<? extends Token> columnList = ((SelectStatementToken) parsedTokens
				.iterator().next()).getColumnList();

		checkForFunctions(columnList, expected);
	}

	private void checkForFunctions(Set<? extends Token> actualColumns,
			Set<Token> expectedColumns) {

		assertEquals(expectedColumns.size(), actualColumns.size());

		for (Token actualColumn : actualColumns) {
			assertTrue(actualColumn instanceof FunctionToken);
			FunctionToken actual = (FunctionToken) actualColumn;
			assertTrue(expectedColumns.contains(actual));
		}

	}

	@Test
	public void testColumnCalculations() {
		String sql = "SELECT col1 * 12 AS calc, 25+3 FROM users";
		Set<CalculationToken> expected = new HashSet<>();

		expected.add(new CalculationToken("col1*12"));
		expected.add(new CalculationToken("25+3"));

		Set<Token> parsedTokens = parser.parse(sql);

		Set<? extends Token> actual = ((SelectStatementToken) parsedTokens
				.iterator().next()).getColumnList();

		assertEquals(expected.size(), actual.size());
		assertTrue(actual.containsAll(expected));
	}

}
