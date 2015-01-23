package de.alksa.parser.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.ColumnNameToken;
import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.TableNameToken;
import de.alksa.token.Token;

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

		Set<Token> columnListSubquery = new HashSet<>(
				Arrays.asList(new ColumnNameToken("x")));
		Set<Token> fromListSubquery = new HashSet<>(
				Arrays.asList(new TableNameToken("b")));

		SelectStatementToken subquery = new SelectStatementToken();
		subquery.setColumnList(columnListSubquery);
		subquery.setFromList(fromListSubquery);

		Set<Token> columnListTopLevel = new HashSet<>(Arrays.asList(subquery));
		Set<Token> fromListTopLevel = new HashSet<>(
				Arrays.asList(new TableNameToken("tab")));

		SelectStatementToken expected = new SelectStatementToken();
		expected.setColumnList(columnListTopLevel);
		expected.setFromList(fromListTopLevel);

		Token actual = parser.parse(sql).iterator().next();

		assertEquals(expected, actual);
	}

	@Test
	public void testSubqueryInFromList() {
		String sql = "SELECT a FROM (SELECT x FROM b) subAlias";

		Set<Token> columnListSubquery = new HashSet<>(
				Arrays.asList(new ColumnNameToken("x")));
		Set<Token> fromListSubquery = new HashSet<>(
				Arrays.asList(new TableNameToken("b")));

		SelectStatementToken subquery = new SelectStatementToken();
		subquery.setColumnList(columnListSubquery);
		subquery.setFromList(fromListSubquery);

		Set<Token> columnListTopLevel = new HashSet<>(
				Arrays.asList(new ColumnNameToken("a")));
		Set<Token> fromListTopLevel = new HashSet<>(Arrays.asList(subquery));

		SelectStatementToken expected = new SelectStatementToken();
		expected.setColumnList(columnListTopLevel);
		expected.setFromList(fromListTopLevel);

		Token actual = parser.parse(sql).iterator().next();

		assertEquals(expected, actual);
	}

	@Test
	public void testSubqueryInWhereClause() {
		String sql = "SELECT a FROM b WHERE a = (SELECT x FROM b)";

		Set<Token> columnListSubquery = new HashSet<>(
				Arrays.asList(new ColumnNameToken("x")));
		Set<Token> fromListSubquery = new HashSet<>(
				Arrays.asList(new TableNameToken("b")));

		SelectStatementToken subquery = new SelectStatementToken();
		subquery.setColumnList(columnListSubquery);
		subquery.setFromList(fromListSubquery);

		Set<Token> columnListTopLevel = new HashSet<>(
				Arrays.asList(new ColumnNameToken("a")));
		Set<Token> fromListTopLevel = new HashSet<>(
				Arrays.asList(new TableNameToken("b")));

		Set<Token> whereClauseTopLevel = new HashSet<>(
				Arrays.asList(new ComparisonFilterToken(
						new ColumnNameToken("a"),
						ComparisonFilterToken.Type.EQUAL, subquery)));

		SelectStatementToken expected = new SelectStatementToken();
		expected.setColumnList(columnListTopLevel);
		expected.setFromList(fromListTopLevel);
		expected.setWhereClause(whereClauseTopLevel);

		Token actual = parser.parse(sql).iterator().next();

		assertEquals(expected, actual);
	}
}
