package de.alksa.querystorage.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.alksa.token.Token;

public class QueryImplTest {

	@Test
	public void testGettersNonNull() {
		List<Token> sqlQuery = new ArrayList<>();
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
