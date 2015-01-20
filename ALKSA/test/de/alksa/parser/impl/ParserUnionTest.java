package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.ColumnNameToken;
import de.alksa.token.FromListToken;
import de.alksa.token.SelectColumnListToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.TableNameToken;
import de.alksa.token.Token;

public class ParserUnionTest {

	private VisitorBasedParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new VisitorBasedParser();
	}

	@Test
	public void testUnion() {
		String sql = "SELECT a FROM t1 UNION SELECT b FROM t2";
		List<SelectStatementToken> expected = new ArrayList<>();
		List<Token> firstSelectTokens = new ArrayList<>();
		List<Token> secondSelectTokens = new ArrayList<>();

		firstSelectTokens.add(new SelectColumnListToken(Arrays
				.asList(new ColumnNameToken("a"))));
		firstSelectTokens.add(new FromListToken(Arrays
				.asList(new TableNameToken("t1"))));

		secondSelectTokens.add(new SelectColumnListToken(Arrays
				.asList(new ColumnNameToken("b"))));
		secondSelectTokens.add(new FromListToken(Arrays
				.asList(new TableNameToken("t2"))));

		expected.add(new SelectStatementToken(firstSelectTokens));
		expected.add(new SelectStatementToken(secondSelectTokens));

		List<Token> actual = parser.parse(sql);

		assertEquals(2, actual.size());
		assertTrue(actual.containsAll(expected));
	}
}
