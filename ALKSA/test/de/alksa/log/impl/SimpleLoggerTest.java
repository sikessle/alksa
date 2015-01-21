package de.alksa.log.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.log.LogEntry;
import de.alksa.persistence.StorageDao;

public class SimpleLoggerTest {

	private SimpleLogger logger;
	private List<LogEntry> expectedEntries;
	private StorageDao storageMock;

	@Before
	public void setUp() throws Exception {
		expectedEntries = new ArrayList<>();
		expectedEntries.add(new AttackLogEntry("", "", "", ""));

		storageMock = mock(StorageDao.class);
		when(storageMock.getLogEntries()).thenReturn(expectedEntries);

		logger = new SimpleLogger(storageMock);
	}

	@Test
	public void testRead() {
		List<LogEntry> readEntries = logger.read();

		assertEquals(expectedEntries.size(), readEntries.size());

		for (LogEntry readEntry : readEntries) {
			assertTrue(expectedEntries.contains(readEntry));
		}
	}

	@Test
	public void testWrite() {
		// to avoid concurrent modification exception
		List<LogEntry> entriesToAdd = new ArrayList<>();
		entriesToAdd.addAll(expectedEntries);

		for (LogEntry entry : entriesToAdd) {
			logger.write(entry);
			verify(storageMock, atLeastOnce()).saveLogEntries(any());
		}

		// should not be added
		logger.write(null);
		
		assertEquals(expectedEntries.size(), logger.read().size());
	}

}
