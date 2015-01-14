package de.alksa.querystorage.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.alksa.persistence.StorageDao;
import de.alksa.querystorage.Query;

public class SimpleQueryStorageTest {

	private SimpleQueryStorage queryStorage;
	private List<Query> expectedQueries;

	@Before
	public void setUp() throws Exception {
		expectedQueries = new ArrayList<>();
		expectedQueries.add(new QueryImpl("", "", ""));

		StorageDao storageMock = mock(StorageDao.class);
		when(storageMock.getQueries()).thenReturn(expectedQueries);

		queryStorage = new SimpleQueryStorage(storageMock);
	}

	@Test
	public void testRead() {
		List<Query> readQueries = queryStorage.read();

		assertEquals(expectedQueries.size(), readQueries.size());

		for (Query readQuery : readQueries) {
			assertTrue(expectedQueries.contains(readQuery));
		}
	}

	@Test
	public void testWrite() {
		// to avoid concurrent modification exception
		List<Query> queriesToAdd = new ArrayList<>();
		queriesToAdd.addAll(expectedQueries);

		for (Query query : queriesToAdd) {
			queryStorage.write(query);
		}

		// should not be added
		queryStorage.write(null);
	}

}
