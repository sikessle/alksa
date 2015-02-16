package de.alksa.parser.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.alksa.token.ColumnNameToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.TableNameToken;
import de.alksa.token.Token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserUnionTest extends ParserTest {

	@Test
	public void testUnion() {
		String sql = "SELECT a FROM t1 UNION SELECT b FROM t2";
		Set<SelectStatementToken> expected = new HashSet<>();

		SelectStatementToken firstSelect = new SelectStatementToken();
		firstSelect.setColumnList(new HashSet<>(Arrays
				.asList(new ColumnNameToken("a"))));
		firstSelect.setFromList(new HashSet<>(Arrays.asList(new TableNameToken(
				"t1"))));

		SelectStatementToken secondSelect = new SelectStatementToken();
		secondSelect.setColumnList(new HashSet<>(Arrays
				.asList(new ColumnNameToken("b"))));
		secondSelect.setFromList(new HashSet<>(Arrays
				.asList(new TableNameToken("t2"))));

		expected.add(firstSelect);
		expected.add(secondSelect);

		Set<Token> actual = exceptionSafeParse(sql);

		assertEquals(2, actual.size());
		assertTrue(actual.containsAll(expected));
	}
}
