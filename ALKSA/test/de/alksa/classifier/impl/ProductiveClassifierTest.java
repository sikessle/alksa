package de.alksa.classifier.impl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import de.alksa.log.Logger;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;
import static org.mockito.Mockito.*;

public class ProductiveClassifierTest extends StateClassifierTest {

	private ProductiveClassifier classifier;
	private QueryStorage queryStorageMock;
	private Logger loggerMock;
	private Set<QueryTestSet> querySets;
	private Set<Query> learnedQueries;

	@Before
	public void setUp() throws Exception {
		queryStorageMock = mock(QueryStorage.class);
		loggerMock = mock(Logger.class);
		classifier = new ProductiveClassifier(queryStorageMock, loggerMock);
		learnedQueries = new HashSet<>();
		querySets = new HashSet<>();

		initQuerySets();
		initLearnedQueries();

		when(queryStorageMock.read()).thenReturn(learnedQueries);
	}

	private void initQuerySets() {
		addQuerySet1();
	}

	private void addQuerySet1() {
		Set<Query> allowed = new HashSet<>();
		Set<Query> disallowed = new HashSet<>();

		Query learned = createQuery("SELECT col FROM users", "db-test",
				"tester");

		allowed.add(createQuery("SELECT col FROM users", "db-test", "tester"));

		disallowed.add(createQuery("SELECT pass FROM users", "db-test",
				"tester"));

		querySets.add(new QueryTestSet(learned, allowed, disallowed));
	}

	private void initLearnedQueries() {
		for (QueryTestSet testSet : querySets) {
			learnedQueries.add(testSet.getLearned());
		}
	}

	@Test
	public void testAccceptEqualQueries() {
		for (Query query : learnedQueries) {
			assertTrue(classifier.accept(query));
			verify(queryStorageMock).read();
		}
	}

	@Test(expected = NullPointerException.class)
	public void testAcceptWithNPE() {
		classifier.accept(null);
	}
}
