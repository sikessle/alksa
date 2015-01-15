package de.alksa.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.token.FromListToken;
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
		String sql = "SELECT c1 FROM users u";
		List<TableNameToken> expected = new ArrayList<>();
		List<? extends Token> actual;

		expected.add(new TableNameToken("users"));

		List<Token> tokens = parser.parse(sql);
		
		// otherwise loop could be skipped
		assertTrue(tokens.size() > 0);

		for (Token token : tokens) {
			if (token instanceof FromListToken) {
				actual = ((FromListToken) token).getChildren();
				
				for (Token t : actual) {
					System.out.println(t);
				}
				
				assertEquals(expected.size(), actual.size());
				assertTrue(actual.containsAll(expected));
			}
		}
	}

}
