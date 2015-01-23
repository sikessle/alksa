package de.alksa.persistence.impl;

import java.io.File;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.alksa.log.LogEntry;
import de.alksa.log.impl.AttackLogEntry;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.impl.SingleSelectQuery;
import de.alksa.token.SelectStatementToken;

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
		expectedEntries.add(new AttackLogEntry("", "", "", "", Instant.now()));

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

}
