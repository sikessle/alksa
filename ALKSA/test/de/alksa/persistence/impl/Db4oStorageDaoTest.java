package de.alksa.persistence.impl;

import java.io.File;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.alksa.log.LogEntry;
import de.alksa.log.impl.LogEntryImpl;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.impl.SingleSelectQuery;
import de.alksa.token.BinaryLogicalFilterToken;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Db4oStorageDaoTest {

	private Db4oStorageDao storage;
	private String testPath = "/tmp/alksa-test";

	@Before
	public void setUp() throws Exception {
		new File(testPath).delete();
		storage = new Db4oStorageDao(testPath);
	}

	@After
	public void tearDown() throws Exception {
		storage.close();
		new File(testPath).delete();
	}

	@Test
	public void testLogEntries() {
		Set<LogEntry> expectedEntries = new HashSet<>();
		expectedEntries.add(new LogEntryImpl("", "", "", "", Instant.now()));

		storage.saveLogEntries(expectedEntries);
		Set<LogEntry> actualEntries = storage.getLogEntries();

		assertEquals(expectedEntries.size(), actualEntries.size());

		for (LogEntry expectedEntry : expectedEntries) {
			assertTrue(actualEntries.contains(expectedEntry));
		}
	}

	@Test
	public void testQueries() {
		Set<Query> expectedQueries = new HashSet<>();
		expectedQueries.add(new SingleSelectQuery(new SelectStatementToken(),
				"", "", ""));

		storage.saveQueries(expectedQueries);
		Set<Query> actualQueries = storage.getQueries();

		assertEquals(expectedQueries.size(), actualQueries.size());

		for (Query expectedEntry : expectedQueries) {
			assertTrue(actualQueries.contains(expectedEntry));
		}
	}

	@Test
	public void testQueriesDepth() {
		Set<Query> expectedQueries = new HashSet<>();
		SelectStatementToken select = new SelectStatementToken();
		Set<Token> whereClause = new HashSet<>();

		ComparisonFilterToken comparison1 = new ComparisonFilterToken(
				new ColumnNameToken("a"), ComparisonFilterToken.Type.EQUAL,
				new ColumnNameToken("b"));
		ComparisonFilterToken comparison2 = new ComparisonFilterToken(
				new ColumnNameToken("d"), ComparisonFilterToken.Type.EQUAL,
				new ColumnNameToken("e"));
		whereClause.add(new BinaryLogicalFilterToken(
				BinaryLogicalFilterToken.Type.AND, comparison1, comparison2));

		select.setWhereClause(whereClause);

		Query expectedQuery = new SingleSelectQuery(select, "", "", "");
		expectedQueries.add(expectedQuery);

		storage.saveQueries(expectedQueries);
		storage.close();
		storage = new Db4oStorageDao(testPath);

		Set<Query> actualQueries = storage.getQueries();
		assertEquals(1, actualQueries.size());
		Query actualQuery = actualQueries.iterator().next();

		System.out.println(expectedQuery.getSelectStatement());
		System.out.println(actualQuery.getSelectStatement());

		assertEquals(actualQuery, expectedQuery);
	}

}
