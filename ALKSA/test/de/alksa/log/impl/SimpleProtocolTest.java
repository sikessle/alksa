package de.alksa.log.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.log.LogEntry;
import de.alksa.persistence.StorageDao;

public class SimpleProtocolTest {

	private SimpleProtocol protocol;
	private List<LogEntry> entries;

	@Before
	public void setUp() throws Exception {
		entries = new ArrayList<>();
		entries.add(new AttackLogEntry(null, null, null, null));

		StorageDao storageMock = mock(StorageDao.class);
		when(storageMock.getProtocolEntries()).thenReturn(entries);

		protocol = new SimpleProtocol(storageMock);
	}

	@Test
	public void testRead() {
		List<LogEntry> readEntries = protocol.read();

		assertEquals(entries.size(), readEntries.size());

		for (LogEntry readEntry : readEntries) {
			assertTrue(entries.contains(readEntry));
		}
	}

	@Test
	public void testWrite() {
		// to avoid concurrent modification exception
		List<LogEntry> entriesToAdd = new ArrayList<>();
		entriesToAdd.addAll(entries);

		for (LogEntry entry : entriesToAdd) {
			protocol.write(entry);
		}

		// should not be added
		protocol.write(null);
	}

}
