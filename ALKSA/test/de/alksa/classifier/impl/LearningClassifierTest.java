package de.alksa.classifier.impl;

import org.junit.Before;
import org.junit.Test;

import de.alksa.log.Logger;
import de.alksa.parser.Parser;
import de.alksa.querystorage.QueryStorage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.*;

public class LearningClassifierTest {

	private LearningClassifier classifier;
	private QueryStorage queryStorageMock;

	@Before
	public void setUp() throws Exception {
		queryStorageMock = mock(QueryStorage.class);

		classifier = new LearningClassifier(queryStorageMock);
	}

	@Test
	public void testLearningMode() {
		// classifier.setLearning(true);
		// assertTrue(classifier.accept(query, database, databaseUser));
		//
		// verifyLearningMocks();
	}

	private void verifyLearningMocks() {
		// verify(parserMock).parse(query);
	}

}
