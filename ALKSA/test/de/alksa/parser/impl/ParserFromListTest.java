package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.FromListToken;
import de.alksa.token.JoinToken;
import de.alksa.token.TableNameToken;
import de.alksa.token.Token;

public class ParserFromListTest {

	private VisitorBasedParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new VisitorBasedParser();
	}

	@Test
	public void testTableName() {
		String sql = "SELECT c1 FROM users, depart";
		List<TableNameToken> expected = new ArrayList<>();
		List<? extends Token> actual;
		boolean fromListTokenExists = false;

		expected.add(new TableNameToken("users"));
		expected.add(new TableNameToken("depart"));

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof FromListToken) {
				actual = ((FromListToken) token).getChildren();

				assertEquals(expected.size(), actual.size());
				assertTrue(actual.containsAll(expected));

				fromListTokenExists = true;
			}
		}

		if (!fromListTokenExists) {
			fail("No FromListToken found");
		}
	}

	@Test
	public void testTableNameAlias() {
		String sql = "SELECT c1 FROM users u, ignore1 CROSS JOIN ignore2";
		List<TableNameToken> expected = new ArrayList<>();
		List<? extends Token> actual;

		expected.add(new TableNameToken("users"));

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof FromListToken) {
				actual = ((FromListToken) token).getChildren();

				// minus 1 because of the ignored join
				assertEquals(expected.size(), actual.size() - 1);
				assertTrue(actual.containsAll(expected));
			}
		}
	}

	@Test
	public void testSimpleJoinWithoutOnClause() {
		testJoinType("INNER", "on c1 = c2");
		testJoinType("NATURAL", "");
		testJoinType("LEFT OUTER", "on c1 = c2");
		testJoinType("RIGHT OUTER", "on c1 = c2");
		testJoinType("FULL OUTER", "on c1 = c2");
	}

	private void testJoinType(String joinType, String onClause) {
		String sql = "SELECT c1 FROM users " + joinType + " JOIN departments "
				+ onClause;
		List<JoinToken> expected = new ArrayList<>();
		List<? extends Token> actual;

		expected.add(new JoinToken(joinType, new TableNameToken("users"),
				new TableNameToken("departments")));

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof FromListToken) {
				actual = ((FromListToken) token).getChildren();

				assertEquals(expected.size(), actual.size());
				assertTrue(actual.containsAll(expected));
			}
		}
	}

	@Test
	public void testMultiJoinWithoutOnClause() {
		// (ll LEFT lr) RIGHT rr
		String sql = "SELECT c1 FROM ll LEFT OUTER JOIN lr on c1 = c2 RIGHT OUTER JOIN rr on c3 = c4";
		List<JoinToken> expected = new ArrayList<>();
		List<? extends Token> actual;

		JoinToken leftJoin = new JoinToken("LEFT OUTER", new TableNameToken(
				"ll"), new TableNameToken("lr"));

		// top level join
		JoinToken rightJoin = new JoinToken("RIGHT OUTER", leftJoin,
				new TableNameToken("rr"));

		expected.add(rightJoin);

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof FromListToken) {
				actual = ((FromListToken) token).getChildren();

				assertEquals(expected.size(), actual.size());
				assertTrue(actual.containsAll(expected));
			}
		}
	}

	@Test
	public void testJoinWithOnClause() {
		String sql = "SELECT c1 FROM left LEFT OUTER JOIN right ON left.columnLeft = right.columnRight";
		List<JoinToken> expected = new ArrayList<>();
		List<? extends Token> actual;

		JoinToken leftJoin = new JoinToken("LEFT OUTER", new TableNameToken(
				"ll"), new TableNameToken("lr"));

		// top level join
		JoinToken rightJoin = new JoinToken("RIGHT OUTER", leftJoin,
				new TableNameToken("rr"));

		expected.add(rightJoin);

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof FromListToken) {
				actual = ((FromListToken) token).getChildren();

				assertEquals(expected.size(), actual.size());
				assertTrue(actual.containsAll(expected));
			}
		}
	}

}
