package de.alksa.querystorage.impl;

import org.junit.Test;

import de.alksa.querystorage.Query;
import de.alksa.token.SelectStatementToken;

import static org.junit.Assert.assertEquals;

public class SingleSelectQueryTest {

	@Test
	public void testGettersNonNull() {
		SelectStatementToken select = new SelectStatementToken();
		String queryString = "SELECT";
		String database = "testDatabase";
		String databaseUser = "root";

		Query query = new SingleSelectQuery(select, queryString,
				database, databaseUser);

		assertEquals(select, query.getSelectStatement());
		assertEquals(queryString, query.getQueryString());
		assertEquals(database, query.getDatabase());
		assertEquals(databaseUser, query.getDatabaseUser());
	}

	@Test(expected = NullPointerException.class)
	public void testGettersNull() {
		new SingleSelectQuery(null, null, null, null);
	}

}
