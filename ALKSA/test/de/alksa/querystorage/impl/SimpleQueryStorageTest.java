package de.alksa.querystorage.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.alksa.persistence.StorageDao;
import de.alksa.querystorage.Query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SimpleQueryStorageTest {

	private SimpleQueryStorage queryStorage;
	private Set<Query> expectedQueries;
	private StorageDao storageMock;

	@Before
	public void setUp() throws Exception {
		expectedQueries = new HashSet<>();
		expectedQueries.add(new QueryImpl(new ArrayList<>(), "", "", ""));
		expectedQueries.add(new QueryImpl(new ArrayList<>(), "x", "y", "z"));

		storageMock = mock(StorageDao.class);
		when(storageMock.getQueries()).thenReturn(expectedQueries);

		queryStorage = new SimpleQueryStorage(storageMock);
	}

	@Test
	public void testRead() {
		Set<Query> readQueries = queryStorage.read();

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
			// to check if the same query only gets added once
			queryStorage.write(query);
			verify(storageMock, atLeastOnce()).saveQueries(any());
		}

		assert (queryStorage.read().containsAll(expectedQueries));

		// should not be added
		queryStorage.write(null);

		assertEquals(expectedQueries.size(), queryStorage.read().size());
	}

}
