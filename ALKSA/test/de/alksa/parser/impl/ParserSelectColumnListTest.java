package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.CalculationToken;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.FunctionToken;
import de.alksa.token.SelectColumnListToken;
import de.alksa.token.Token;

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
		List<ColumnNameToken> expected = new ArrayList<>();
		List<? extends Token> actual;
		boolean columnListTokenExists = false;

		expected.add(new ColumnNameToken("users.col1"));
		expected.add(new ColumnNameToken("col2"));

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof SelectColumnListToken) {
				actual = ((SelectColumnListToken) token).getChildren();

				// minus 1 because of the ABS(col3) column
				assertEquals(expected.size(), actual.size() - 1);
				assertTrue(actual.containsAll(expected));

				columnListTokenExists = true;
			}
		}

		if (!columnListTokenExists) {
			fail("No SelectColumnListToken found");
		}
	}

	@Test
	public void testColumnNameWithAsterisk() {
		String sql = "SELECT * FROM users";
		List<ColumnNameToken> expected = new ArrayList<>();
		List<? extends Token> actual;
		boolean columnListTokenExists = false;

		expected.add(new ColumnNameToken("*"));

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof SelectColumnListToken) {
				actual = ((SelectColumnListToken) token).getChildren();

				assertEquals(expected.size(), actual.size());
				assertTrue(actual.containsAll(expected));

				columnListTokenExists = true;
			}
		}

		if (!columnListTokenExists) {
			fail("No SelectColumnListToken found");
		}
	}

	@Test
	public void testColumnListWithFunctions() {
		String sql = "SELECT ABS(col1), AVG(col2) FROM users";
		List<Token> functionParametersABS = new ArrayList<>();
		List<Token> functionParametersAVG = new ArrayList<>();
		List<Token> expected = new ArrayList<>();

		// ABS(col1)
		functionParametersABS.add(new ColumnNameToken("col1"));
		// AVG(col2)
		functionParametersAVG.add(new ColumnNameToken("col2"));

		expected.add(new FunctionToken("ABS", functionParametersABS));
		expected.add(new FunctionToken("AVG", functionParametersAVG));

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token t : tokens) {
			if (t instanceof SelectColumnListToken) {
				checkForFunctions((SelectColumnListToken) t, expected);
			}
		}

	}

	private void checkForFunctions(SelectColumnListToken selectColumnList,
			List<Token> expectedColumns) {
		List<? extends Token> actualColumns = selectColumnList.getChildren();

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
		List<CalculationToken> expected = new ArrayList<>();
		List<? extends Token> actual;

		expected.add(new CalculationToken("col1*12"));
		expected.add(new CalculationToken("25+3"));

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof SelectColumnListToken) {
				actual = ((SelectColumnListToken) token).getChildren();
				
				assertEquals(expected.size(), actual.size());
				assertTrue(actual.containsAll(expected));
			}
		}
	}

}
