package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.alksa.token.ColumnToken;
import de.alksa.token.SelectListToken;
import de.alksa.token.Token;

public class VisitorBasedParserTest {

	private VisitorBasedParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new VisitorBasedParser();
	}

	@Test
	public void testSelectColumnListWithAlias() {
		String sql = "SELECT col1 AS c1, col2, ABS(col3) FROM users";
		List<ColumnToken> expectedColumnTokens = new ArrayList<>();
		expectedColumnTokens.add(new ColumnToken("col1"));
		expectedColumnTokens.add(new ColumnToken("col2"));
		List<Token> tokens = parser.parse(sql);
		
		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token t : tokens) {
			if (t instanceof SelectListToken) {
				checkColumnList((SelectListToken) t, expectedColumnTokens);
			}
		}
	}

	private void checkColumnList(SelectListToken list, List<ColumnToken> expected) {
		List<Token> children = list.getChildren();
		assertEquals(expected.size(), children.size());
		
		for (Token expectedToken : expected) {
			if (expectedToken instanceof ColumnToken) {
				assertTrue(children.contains(expectedToken));
			}
		}
	}
	
	@Ignore
	public void testSelectFunctionList() {
		String sql = "SELECT col1 AS c1, col2 FROM users";
		List<ColumnToken> expectedColumnTokens = new ArrayList<>();
		expectedColumnTokens.add(new ColumnToken("col1"));
		expectedColumnTokens.add(new ColumnToken("col2"));
		List<Token> tokens = parser.parse(sql);
		
		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token t : tokens) {
			if (t instanceof SelectListToken) {
				checkColumnList((SelectListToken) t, expectedColumnTokens);
			}
		}
	}

}
