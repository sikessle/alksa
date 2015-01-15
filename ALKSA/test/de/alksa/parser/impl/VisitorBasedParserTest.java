package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.alksa.token.ColumnToken;
import de.alksa.token.FunctionToken;
import de.alksa.token.SelectColumnListToken;
import de.alksa.token.Token;

public class VisitorBasedParserTest {

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
	public void testSelectColumnNameWithAlias() {
		// hiddenCol and col3 should not come up in the select column list
		String sql = "SELECT users.col1 AS c1, col2, ABS(col3) FROM users WHERE hiddenCol = 'a'";
		List<ColumnToken> expected = new ArrayList<>();
		List<? extends Token> actual;
		boolean columnListTokenExists = false;

		expected.add(new ColumnToken("users.col1"));
		expected.add(new ColumnToken("col2"));

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
	public void testSelectColumnNameWithAsterisk() {
		String sql = "SELECT * FROM users";
		List<ColumnToken> expected = new ArrayList<>();
		List<? extends Token> actual;
		boolean columnListTokenExists = false;

		expected.add(new ColumnToken("*"));

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
	public void testSelectColumnListWithFunctions() {
		String sql = "SELECT ABS(col1), AVG(col2) FROM users";
		List<Token> functionParametersABS = new ArrayList<>();
		List<Token> functionParametersAVG = new ArrayList<>();
		List<Token> expected = new ArrayList<>();

		// ABS(col1)
		functionParametersABS.add(new ColumnToken("col1"));
		// AVG(col2)
		functionParametersAVG.add(new ColumnToken("col2"));

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
	
	// TODO  !!!!!!!!!!
	@Ignore
	public void testSelectColumnCalculations() {
		// hiddenCol and col3 should not come up in the select column list
		String sql = "SELECT col1 * 12 AS calc FROM users";
		List<ColumnToken> expected = new ArrayList<>();
		List<? extends Token> actual;
		boolean columnListTokenExists = false;

		expected.add(new ColumnToken("calc"));

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

}
