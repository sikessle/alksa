package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	public void testSelectColumnListWithAlias() {
		// hiddenCol should not come up in the select column list
		String sql = "SELECT users.col1 AS c1, col2 FROM users WHERE hiddenCol = 'a'";
		List<ColumnToken> expectedColumnTokens = new ArrayList<>();
		expectedColumnTokens.add(new ColumnToken("users.col1"));
		expectedColumnTokens.add(new ColumnToken("col2"));
		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token t : tokens) {
			if (t instanceof SelectColumnListToken) {
				checkColumnList((SelectColumnListToken) t, expectedColumnTokens);
			}
		}
	}

	private void checkColumnList(SelectColumnListToken list,
			List<ColumnToken> expected) {
		List<? extends Token> children = list.getChildren();
		assertEquals(expected.size(), children.size());

		assertTrue(children.containsAll(expected));
		assertTrue(expected.containsAll(children));
	}

	// TODO REMOVE !!!!!!!!!!!!!!!!
	@Ignore
	public void testSelectColumnListWithFunctions() {
		String sql = "SELECT ABS(col1), CONCAT(col3, col4) FROM users";
		List<Token> functionParametersABS = new ArrayList<>();
		List<Token> functionParametersCONCAT = new ArrayList<>();
		List<Token> expected = new ArrayList<>();

		// ABS(col1)
		functionParametersABS.add(new ColumnToken("col1"));
		// CONCAT(col3, col3)
		functionParametersCONCAT.add(new ColumnToken("col3"));
		functionParametersCONCAT.add(new ColumnToken("col4"));

		expected.add(new FunctionToken("ABS", functionParametersABS));
		expected.add(new FunctionToken("CONCAT", functionParametersCONCAT));

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token t : tokens) {
			if (t instanceof SelectColumnListToken) {
				checkForFunctions((SelectColumnListToken) t, expected);
			}
		}

	}

	private void checkForFunctions(SelectColumnListToken columnList,
			List<Token> expected) {
		List<? extends Token> columns = columnList.getChildren();
		assertEquals(expected.size(), columns.size());

		for (Token column : columns) {
			assertTrue(column instanceof FunctionToken);

			FunctionToken actual = (FunctionToken) column;

			assertEquals(expected, actual);
		}

	}

}
