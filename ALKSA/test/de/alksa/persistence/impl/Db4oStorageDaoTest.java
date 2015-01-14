package de.alksa.persistence.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.alksa.log.LogEntry;
import de.alksa.log.impl.AttackLogEntry;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.impl.QueryImpl;

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
	}

	@Test
	public void testProtocolEntries() {
		List<LogEntry> expectedEntries = new ArrayList<>();
		expectedEntries.add(new AttackLogEntry("", "", "", ""));
		
		storage.saveProtocolEntries(expectedEntries);
		List<LogEntry> actualEntries = storage.getProtocolEntries();
		
		assertEquals(expectedEntries.size(), actualEntries.size());
		
		for (LogEntry expectedEntry : expectedEntries) {
			assertTrue(actualEntries.contains(expectedEntry));
		}
	}
	
	@Test
	public void testQueries() {
		List<Query> expectedQueries = new ArrayList<>();
		expectedQueries.add(new QueryImpl("", "", ""));
		
		storage.saveQueries(expectedQueries); 
		List<Query> actualQueries = storage.getQueries();
		
		assertEquals(expectedQueries.size(), actualQueries.size());
		
		for (Query expectedEntry : expectedQueries) {
			assertTrue(actualQueries.contains(expectedEntry));
		}
	}

}
