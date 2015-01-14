package de.alksa.querystorage.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QueryImplTest {

	@Test
	public void testGettersNonNull() {
		String sqlQuery = "SELECT x FROM y;";
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
