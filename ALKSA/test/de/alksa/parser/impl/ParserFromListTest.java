package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.swing.RowFilter.ComparisonType;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.ColumnNameToken;
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
		testJoinType(JoinToken.Type.INNER, "on c1 = c2");
		testJoinType(JoinToken.Type.NATURAL, "");
		testJoinType(JoinToken.Type.LEFT_OUTER, "on c1 = c2");
		testJoinType(JoinToken.Type.RIGHT_OUTER, "on c1 = c2");
		testJoinType(JoinToken.Type.FULL_OUTER, "on c1 = c2");
	}

	private void testJoinType(JoinToken.Type joinType, String onClause) {
		String sql = "SELECT c1 FROM users " + joinType + " JOIN departments "
				+ onClause;
		List<JoinToken> expected = new ArrayList<>();
		List<? extends Token> actual;

		expected.add(new JoinToken(new TableNameToken("users"), joinType,
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

		JoinToken leftJoin = new JoinToken(new TableNameToken("ll"),
				JoinToken.Type.LEFT_OUTER, new TableNameToken("lr"));

		// top level join
		JoinToken rightJoin = new JoinToken(leftJoin,
				JoinToken.Type.RIGHT_OUTER, new TableNameToken("rr"));

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
		String sql = "SELECT c1 FROM leftTable LEFT OUTER JOIN rightTable ON leftTable.columnLeft = rightTable.columnRight";
		List<? extends Token> children;
		Token actual = null;

		JoinToken expected = new JoinToken(new TableNameToken("leftTable"),
				JoinToken.Type.LEFT_OUTER, new TableNameToken("rightTable"));
		
		expected.setOnClause(new ComparisonFilterToken(new ColumnNameToken(
				"leftTable.columnLeft"), ComparisonFilterToken.Type.EQUAL,
				new ColumnNameToken("rightTable.columnRight")));

		List<Token> tokens = parser.parse(sql);

		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof FromListToken) {
				children = ((FromListToken) token).getChildren();

				assertEquals(1, children.size());
				actual = children.get(0);
			}
		}

		if (actual == null || !(actual instanceof JoinToken)) {
			fail("Join Token not found");
		}

		JoinToken actualJoinToken = (JoinToken) actual;

		assertTrue(actualJoinToken.hasOnClause());

		assertEquals(expected, actualJoinToken);

	}
}
