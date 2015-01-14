package de.alksa.persistence.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.log.LogEntry;
import de.alksa.log.impl.AttackLogEntry;

public class Db4oStorageDaoTest {

	private Db4oStorageDao storage;
	private String testPath = "/tmp/alksa-test";

	@Before
	public void setUp() throws Exception {
		new File(testPath).delete();
		storage = new Db4oStorageDao(testPath);
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

}
