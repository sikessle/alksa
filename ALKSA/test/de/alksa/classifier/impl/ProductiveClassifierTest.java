package de.alksa.classifier.impl;

import org.junit.Before;
import org.junit.Test;

import de.alksa.log.Logger;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProductiveClassifierTest extends StateClassifierTest {

	private ProductiveClassifier classifier;
	private QueryStorage queryStorageMock;
	private Logger loggerMock;
	private Query query;

	@Before
	public void setUp() throws Exception {
		queryStorageMock = mock(QueryStorage.class);
		loggerMock = mock(Logger.class);

		classifier = new ProductiveClassifier(queryStorageMock, loggerMock);
		query = createQuery("SELECT name FROM users WHERE x = 2", "local",
				"tester");
	}

	@Test
	public void testAccept() {
		assertTrue(classifier.accept(query));
		verify(queryStorageMock).read();
	}

	@Test(expected = NullPointerException.class)
	public void testAcceptWithNPE() {
		assertTrue(classifier.accept(null));
	}
}
