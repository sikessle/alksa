package de.alksa.querystorage.impl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.alksa.token.Token;

import static org.junit.Assert.assertEquals;

public class QueryImplTest {

	@Test
	public void testGettersNonNull() {
		Set<Token> sqlQuery = new HashSet<>();
		String queryString = "SELECT";
		String database = "testDatabase";
		String databaseUser = "root";

		QueryImpl query = new QueryImpl(sqlQuery, queryString, database,
				databaseUser);

		assertEquals(sqlQuery, query.getQuery());
		assertEquals(queryString, query.getQueryString());
		assertEquals(database, query.getDatabase());
		assertEquals(databaseUser, query.getDatabaseUser());
	}

	@Test(expected = NullPointerException.class)
	public void testGettersNull() {
		new QueryImpl(null, null, null, null);
	}

}
