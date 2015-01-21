package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.ColumnNameToken;
import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.FromListToken;
import de.alksa.token.SelectColumnListToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.TableNameToken;
import de.alksa.token.Token;
import de.alksa.token.WhereClauseToken;

public class ParserSubqueryTest {

	private VisitorBasedParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new VisitorBasedParser();
	}

	@Test
	public void testSubqueryInSelectList() {
		String sql = "SELECT (SELECT x FROM b) AS sub FROM tab";

		Token columnListSubquery = new SelectColumnListToken(
				Arrays.asList(new ColumnNameToken("x")));
		Token fromTableSubquery = new FromListToken(
				Arrays.asList(new TableNameToken("b")));

		Token subquery = new SelectStatementToken(Arrays.asList(
				columnListSubquery, fromTableSubquery));

		Token columnListTopLevel = new SelectColumnListToken(
				Arrays.asList(subquery));
		Token fromTableTopLevel = new FromListToken(
				Arrays.asList(new TableNameToken("tab")));

		SelectStatementToken expected = new SelectStatementToken(Arrays.asList(
				columnListTopLevel, fromTableTopLevel));

		Token actual = parser.parse(sql).get(0);

		assertEquals(expected, actual);
	}

	@Test
	public void testSubqueryInFromList() {
		String sql = "SELECT a FROM (SELECT x FROM b) subAlias";

		Token columnListSubquery = new SelectColumnListToken(
				Arrays.asList(new ColumnNameToken("x")));
		Token fromTableSubquery = new FromListToken(
				Arrays.asList(new TableNameToken("b")));

		Token subquery = new SelectStatementToken(Arrays.asList(
				columnListSubquery, fromTableSubquery));

		Token columnListTopLevel = new SelectColumnListToken(
				Arrays.asList(new ColumnNameToken("a")));
		Token fromTableTopLevel = new FromListToken(Arrays.asList(subquery));

		SelectStatementToken expected = new SelectStatementToken(Arrays.asList(
				columnListTopLevel, fromTableTopLevel));

		Token actual = parser.parse(sql).get(0);

		assertEquals(expected, actual);
	}

	@Test
	public void testSubqueryInWhereClause() {
		String sql = "SELECT a FROM b WHERE a = (SELECT x FROM b)";

		Token columnListSubquery = new SelectColumnListToken(
				Arrays.asList(new ColumnNameToken("x")));
		Token fromTableSubquery = new FromListToken(
				Arrays.asList(new TableNameToken("b")));

		Token subquery = new SelectStatementToken(Arrays.asList(
				columnListSubquery, fromTableSubquery));

		Token columnListTopLevel = new SelectColumnListToken(
				Arrays.asList(new ColumnNameToken("a")));
		Token fromTableTopLevel = new FromListToken(
				Arrays.asList(new TableNameToken("b")));

		Token whereClauseTopLevel = new WhereClauseToken(
				Arrays.asList(new ComparisonFilterToken(
						new ColumnNameToken("a"),
						ComparisonFilterToken.Type.EQUAL, subquery)));

		SelectStatementToken expected = new SelectStatementToken(Arrays.asList(
				columnListTopLevel, fromTableTopLevel, whereClauseTopLevel));

		Token actual = parser.parse(sql).get(0);

		assertEquals(expected, actual);
	}
}
