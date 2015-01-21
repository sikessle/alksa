package de.alksa.classifier.impl;

import org.junit.Before;
import org.junit.Test;

import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.*;

public class LearningClassifierTest extends StateClassifierTest {

	private LearningClassifier classifier;
	private QueryStorage queryStorageMock;
	private Query query;

	@Before
	public void setUp() throws Exception {
		queryStorageMock = mock(QueryStorage.class);

		classifier = new LearningClassifier(queryStorageMock);
		query = createQuery("SELECT name FROM users WHERE x = 2", "local",
				"tester");
	}

	@Test
	public void testAccept() {
		assertTrue(classifier.accept(query));
		verify(queryStorageMock).write(query);
	}
}
