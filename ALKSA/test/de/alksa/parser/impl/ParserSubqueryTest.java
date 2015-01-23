package de.alksa.parser.impl;

import java.util.Arrays;
import java.util.HashSet;

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

import static org.junit.Assert.assertEquals;

public class ParserSubqueryTest {

	private VisitorBasedParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new VisitorBasedParser();
	}

	@Test
	public void testSubqueryInSelectList() {
		String sql = "SELECT (SELECT x FROM b) AS sub FROM tab";

		SelectColumnListToken columnListSubquery = new SelectColumnListToken(
				new HashSet<>(Arrays.asList(new ColumnNameToken("x"))));
		FromListToken fromListSubquery = new FromListToken(new HashSet<>(
				Arrays.asList(new TableNameToken("b"))));

		SelectStatementToken subquery = new SelectStatementToken();
		subquery.setColumnListToken(columnListSubquery);
		subquery.setFromListToken(fromListSubquery);

		SelectColumnListToken columnListTopLevel = new SelectColumnListToken(
				new HashSet<>(Arrays.asList(subquery)));
		FromListToken fromListTopLevel = new FromListToken(new HashSet<>(
				Arrays.asList(new TableNameToken("tab"))));

		SelectStatementToken expected = new SelectStatementToken();
		expected.setColumnListToken(columnListTopLevel);
		expected.setFromListToken(fromListTopLevel);

		Token actual = parser.parse(sql).iterator().next();

		assertEquals(expected, actual);
	}

	@Test
	public void testSubqueryInFromList() {
		String sql = "SELECT a FROM (SELECT x FROM b) subAlias";

		SelectColumnListToken columnListSubquery = new SelectColumnListToken(
				new HashSet<>(Arrays.asList(new ColumnNameToken("x"))));
		FromListToken fromListSubquery = new FromListToken(new HashSet<>(
				Arrays.asList(new TableNameToken("b"))));

		SelectStatementToken subquery = new SelectStatementToken();
		subquery.setColumnListToken(columnListSubquery);
		subquery.setFromListToken(fromListSubquery);

		SelectColumnListToken columnListTopLevel = new SelectColumnListToken(
				new HashSet<>(Arrays.asList(new ColumnNameToken("a"))));
		FromListToken fromListTopLevel = new FromListToken(new HashSet<>(
				Arrays.asList(subquery)));

		SelectStatementToken expected = new SelectStatementToken();
		expected.setColumnListToken(columnListTopLevel);
		expected.setFromListToken(fromListTopLevel);

		Token actual = parser.parse(sql).iterator().next();

		assertEquals(expected, actual);
	}

	@Test
	public void testSubqueryInWhereClause() {
		String sql = "SELECT a FROM b WHERE a = (SELECT x FROM b)";

		SelectColumnListToken columnListSubquery = new SelectColumnListToken(
				new HashSet<>(Arrays.asList(new ColumnNameToken("x"))));
		FromListToken fromListSubquery = new FromListToken(new HashSet<>(
				Arrays.asList(new TableNameToken("b"))));

		SelectStatementToken subquery = new SelectStatementToken();
		subquery.setColumnListToken(columnListSubquery);
		subquery.setFromListToken(fromListSubquery);

		SelectColumnListToken columnListTopLevel = new SelectColumnListToken(
				new HashSet<>(Arrays.asList(new ColumnNameToken("a"))));
		FromListToken fromListTopLevel = new FromListToken(new HashSet<>(
				Arrays.asList(new TableNameToken("b"))));

		WhereClauseToken whereClauseTopLevel = new WhereClauseToken(
				new HashSet<>(Arrays.asList(new ComparisonFilterToken(
						new ColumnNameToken("a"),
						ComparisonFilterToken.Type.EQUAL, subquery))));

		SelectStatementToken expected = new SelectStatementToken();
		expected.setColumnListToken(columnListTopLevel);
		expected.setFromListToken(fromListTopLevel);
		expected.setWhereClauseToken(whereClauseTopLevel);

		Token actual = parser.parse(sql).iterator().next();

		assertEquals(expected, actual);
	}
}
