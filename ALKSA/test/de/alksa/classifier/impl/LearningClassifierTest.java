package de.alksa.classifier.impl;

import org.junit.Before;
import org.junit.Test;

import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;
import de.alksa.querystorage.impl.QueryImpl;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class LearningClassifierTest extends StateClassifierTest {

	private LearningClassifier classifier;
	private QueryStorage queryStorageMock;
	private Query query;
	private Query partialQuery1;
	private Query partialQuery2;

	@Before
	public void setUp() throws Exception {
		queryStorageMock = mock(QueryStorage.class);

		String db = "local";
		String user = "tester";
		String partialSQL1 = "SELECT name FROM users WHERE x = 2";
		String partialSQL2 = "SELECT c FROM d";
		String sql = partialSQL1 + " UNION " + partialSQL2;

		classifier = new LearningClassifier(queryStorageMock);

		query = createQuery(sql, db, user);
		partialQuery1 = createPartialQuery(partialSQL1, sql, db, user);
		partialQuery2 = createPartialQuery(partialSQL2, sql, db, user);
	}

	private Query createPartialQuery(String sqlToParse, String sqlInQuery,
			String db, String user) {

		Query temp = createQuery(sqlToParse, db, user);

		return new QueryImpl(temp.getQuery(), sqlInQuery, db, user);
	}

	@Test
	public void testAccept() {
		assertTrue(classifier.accept(query));
		verify(queryStorageMock, never()).write(query);
		verify(queryStorageMock).write(partialQuery1);
		verify(queryStorageMock).write(partialQuery2);
	}

	@Test(expected = NullPointerException.class)
	public void testAcceptWithNPE() {
		classifier.accept(null);
	}
}
