package de.alksa.classifier.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import de.alksa.checker.QueryChecker;
import de.alksa.checker.impl.CheckerModule;
import de.alksa.log.Logger;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;
import de.alksa.querystorage.impl.SingleSelectQuery;
import de.alksa.token.SelectStatementToken;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProductiveStateIsolatedTest extends StateClassifierTest {

	private ProductiveState classifier;
	private QueryStorage queryStorageMock;
	private Logger loggerMock;

	@Inject
	private Set<QueryChecker> checkers;

	@Before
	public void setUp() throws Exception {
		queryStorageMock = mock(QueryStorage.class);
		loggerMock = mock(Logger.class);

		Injector injector = Guice.createInjector(new CheckerModule());
		injector.injectMembers(this);

		classifier = new ProductiveState(checkers, queryStorageMock, loggerMock);

	}

	@Test(expected = NullPointerException.class)
	public void testAcceptWithNPE() {
		classifier.accept(null);
	}

	@Test
	public void testStorageAccess() {
		Query query = new SingleSelectQuery(new SelectStatementToken(),
				"SELECT * FROM t", "db", "tester");
		classifier.accept(new HashSet<>(Arrays.asList(query)));
		verify(queryStorageMock).read();
	}

}
