package de.alksa.classifier.impl;

import org.junit.Before;
import org.junit.Test;

import de.alksa.log.Logger;
import de.alksa.parser.Parser;
import de.alksa.querystorage.QueryStorage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.*;

public class SimpleClassifierTest {

	private String query;
	private String database;
	private String databaseUser;
	private SimpleClassifier classifier;
	private Parser parserMock;
	private QueryStorage queryStorageMock;
	private Logger loggerMock;

	@Before
	public void setUp() throws Exception {
		query = "SELECT * FROM test";
		database = "localhost";
		databaseUser = "tester";

		parserMock = mock(Parser.class);
		queryStorageMock = mock(QueryStorage.class);
		loggerMock = mock(Logger.class);

		classifier = new SimpleClassifier(parserMock, queryStorageMock,
				loggerMock);
	}

	@Test
	public void testConstructor() {
		classifier.setLearning(false);
		assertFalse(classifier.isLearning());
		classifier.setLearning(true);
		assertTrue(classifier.isLearning());

		assertFalse(classifier.accept(null, null, null));
		assertFalse(classifier.accept("", null, null));
		assertFalse(classifier.accept(null, "", null));
		assertFalse(classifier.accept("", "", null));
		assertFalse(classifier.accept(null, null, ""));
		assertFalse(classifier.accept(null, "", ""));
	}

	@Test
	public void testAcception() {
		classifier.setLearning(true);
		assertTrue(classifier.accept(query, database, databaseUser));

		when(parserMock.parse(any())).thenThrow(new RuntimeException());
		assertFalse(classifier.accept(query, database, databaseUser));
	}

}
