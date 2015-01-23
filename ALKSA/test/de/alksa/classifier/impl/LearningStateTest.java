package de.alksa.classifier.impl;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LearningStateTest extends StateClassifierTest {

	private LearningState classifier;
	private QueryStorage queryStorageMock;
	private Query query;

	@Before
	public void setUp() throws Exception {
		queryStorageMock = mock(QueryStorage.class);

		String db = "local";
		String user = "tester";

		classifier = new LearningState(queryStorageMock);

		query = createQuery("SELECT name FROM users", db, user);
	}

	@Test
	public void testAccept() {
		assertTrue(classifier.accept(new HashSet<>(Arrays.asList(query))));
		verify(queryStorageMock).write(query);
	}

	@Test(expected = NullPointerException.class)
	public void testAcceptWithNPE() {
		classifier.accept(null);
	}
}
