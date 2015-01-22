package de.alksa.classifier.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;

import com.google.inject.Guice;
import com.google.inject.Inject;

import de.alksa.checker.QueryChecker;
import de.alksa.checker.impl.CheckerModule;
import de.alksa.log.LogEntry;
import de.alksa.log.Logger;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

import static org.junit.Assert.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests with same Database and Database User
 */
@RunWith(Parameterized.class)
public class ProductiveClassifierTest extends StateClassifierTest {

	private ProductiveClassifier classifier;
	private QueryStorage queryStorageMock;
	private Logger loggerMock;

	private Query learned;
	private Set<Query> allowed;
	private Set<Query> disallowed;

	private LogEntry latestLog;

	@Inject
	private Set<QueryChecker> checkers;

	private static final String DB = "local";
	private static final String DB_USER = "tester";

	private ArgumentCaptor<LogEntry> logCaptor;

	public ProductiveClassifierTest(String learnedString,
			List<String> allowedStrings, List<String> disallowedStrings) {

		this.learned = createQuery(learnedString, DB, DB_USER);

		Set<Query> allowedQueries = new HashSet<>();
		Set<Query> disallowedQueries = new HashSet<>();

		fillCollectionWithQueries(allowedStrings, allowedQueries);
		fillCollectionWithQueries(allowedStrings, disallowedQueries);

		this.allowed = allowedQueries;
		this.disallowed = disallowedQueries;
	}

	private void fillCollectionWithQueries(List<String> queries,
			Collection<? super Query> target) {
		for (String query : queries) {
			target.add(createQuery(query, DB, DB_USER));
		}
	}

	@Parameters
	public static Collection<Object[]> generateData() {
		Object[][] data = new Object[1][3];

		// learned
		data[0][0] = "SELECT col1 FROM tab";
		// allowed
		data[0][1] = Arrays.asList("SELECT col1 FROM tab",
				"SELECT col1 FROM tab UNION SELECT col1 FROM tab");
		// disallowed TODO zweite Query (SELECT col2) etc. muss abgelehnt
		// werden.
		data[0][2] = Arrays
				.asList("SELECT col1 FROM tab UNION SELECT col2 FROM tab2");

		return Arrays.asList(data);
	}

	@Before
	public void setUp() throws Exception {
		queryStorageMock = mock(QueryStorage.class);
		loggerMock = mock(Logger.class);

		Set<Query> learnedQueries = new HashSet<>();
		Guice.createInjector(new CheckerModule()).injectMembers(this);

		classifier = new ProductiveClassifier(checkers, queryStorageMock,
				loggerMock);

		learnedQueries.add(learned);

		when(queryStorageMock.read()).thenReturn(learnedQueries);

		logCaptor = ArgumentCaptor.forClass(LogEntry.class);
	}

	@Test(expected = NullPointerException.class)
	public void testAcceptWithNPE() {
		classifier.accept(null);
	}

	@Test
	public void testStorageAccess() {
		classifier.accept(learned);
		verify(queryStorageMock).read();
	}

	@Test
	public void testEqualQuery() {
		assertTrue(errorMsg(learned), classifier.accept(learned));
	}

	@Test
	public void testAllowedQueries() {
		for (Query query : allowed) {

			if (!classifier.accept(query)) {
				verify(loggerMock).write(logCaptor.capture());
				latestLog = logCaptor.getValue();
				fail(errorMsg(query));
			} else {
				verify(loggerMock, never()).write(any());
			}

		}
	}

	@Test
	public void testDisallowedQueries() {
		for (Query query : disallowed) {
			assertFalse(errorMsg(query), classifier.accept(query));
			verify(loggerMock).write(
					argThat(new LogEntryWithQuery(query.getQueryString())));
		}
	}

	private String errorMsg(Query checkedQuery) {
		String message = "\nLEARNED [" + learned.getQueryString() + "]";
		message += "\nSUBJECT [" + checkedQuery.getQueryString() + "]";

		if (latestLog != null) {
			message += "\nVIOLATION [" + latestLog.getViolation() + "]";
		}

		return message;
	}

	private static class LogEntryWithQuery extends ArgumentMatcher<LogEntry> {

		private String query;

		public LogEntryWithQuery(String query) {
			this.query = query;
		}

		@Override
		public boolean matches(Object object) {
			LogEntry entry = (LogEntry) object;

			return entry.getQuery().equals(query)
					&& entry.getDatabase().equals(DB)
					&& entry.getDatabaseUser().equals(DB_USER);
		}
	}

}
