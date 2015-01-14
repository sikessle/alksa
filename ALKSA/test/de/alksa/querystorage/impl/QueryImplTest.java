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
		String database = "testDatabase";
		String databaseUser = "root";
		
		QueryImpl query = new QueryImpl(sqlQuery, database,
				databaseUser);

		assertEquals(sqlQuery, query.getQuery());
		assertEquals(database, query.getDatabase());
		assertEquals(databaseUser, query.getDatabaseUser());
	}
	
	@Test(expected=NullPointerException.class)
	public void testGettersNull() {
		new QueryImpl(null, null, null);
	}

}
