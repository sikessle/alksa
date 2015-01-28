package de.alksa.classifier.impl;

import java.io.File;
import java.nio.file.Path;
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
import com.google.inject.Injector;

import de.alksa.ALKSAInvalidQueryException;
import de.alksa.checker.QueryChecker;
import de.alksa.checker.impl.CheckerModule;
import de.alksa.classifier.Classifier;
import de.alksa.log.LogEntry;
import de.alksa.log.Logger;
import de.alksa.parser.Parser;
import de.alksa.parser.impl.ParserModule;
import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests with same Database and Database User
 */
@RunWith(Parameterized.class)
public class ProductiveStateTest extends StateClassifierTest {

	private Classifier classifier;
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

	public ProductiveStateTest(String learnedString,
			List<String> allowedStrings, List<String> disallowedStrings) {

		this.learned = createQueries(learnedString, DB, DB_USER).iterator()
				.next();

		Set<Query> allowedQueries = new HashSet<>();
		Set<Query> disallowedQueries = new HashSet<>();

		fillCollectionWithQueries(allowedStrings, allowedQueries);
		fillCollectionWithQueries(disallowedStrings, disallowedQueries);

		this.allowed = allowedQueries;
		this.disallowed = disallowedQueries;
	}

	private void fillCollectionWithQueries(List<String> queries,
			Collection<Query> target) {
		for (String query : queries) {
			target.addAll(createQueries(query, DB, DB_USER));
		}
	}

	@Parameters
	public static Collection<Object[]> generateData() {
		TestDataParser dataParser = new TestDataParser();

		String pathString = ProductiveStateTest.class.getResource(
				"CheckerTestData").getPath();
		Path path = new File(pathString).toPath();

		Object[][] data = dataParser.parse(path);

		return Arrays.asList(data);
	}

	@Before
	public void setUp() throws Exception {
		queryStorageMock = mock(QueryStorage.class);
		loggerMock = mock(Logger.class);

		Set<Query> learnedQueries = new HashSet<>();
		Injector injector = Guice.createInjector(new CheckerModule(),
				new ParserModule());
		injector.injectMembers(this);

		Parser parser = injector.getInstance(Parser.class);

		classifier = new CheckerBasedClassifier(checkers, parser,
				queryStorageMock, loggerMock);

		learnedQueries.add(learned);

		when(queryStorageMock.read()).thenReturn(learnedQueries);

		logCaptor = ArgumentCaptor.forClass(LogEntry.class);
	}

	@Test
	public void testEqualQuery() {
		assertTrue(errorMsg(learned, "equal queries should be accepted"),
				exceptionSafeAccept(learned.getQueryString(), DB, DB_USER));
	}

	@Test
	public void testAllowedQueries() {
		for (Query query : allowed) {
			reset(loggerMock);
			if (exceptionSafeAccept(query.getQueryString(), DB, DB_USER)) {
				verify(loggerMock, never()).write(any());
			} else {
				verify(loggerMock).write(logCaptor.capture());
				latestLog = logCaptor.getValue();
				fail(errorMsg(query, "Subject is expected to be ACCEPTED"));
			}

		}
	}

	@Test
	public void testDisallowedQueries() {
		for (Query query : disallowed) {
			reset(loggerMock);
			if (exceptionSafeAccept(query.getQueryString(), DB, DB_USER)) {
				verify(loggerMock, never()).write(any());
				fail(errorMsg(query, "Subject is expected to be REJECTED"));
			} else {
				verify(loggerMock).write(
						argThat(new LogEntryWithQuery(query.getQueryString())));
			}
		}
	}

	private boolean exceptionSafeAccept(String sql, String db, String user) {
		try {
			return classifier.accept(sql, db, user);
		} catch (ALKSAInvalidQueryException e) {
			fail("exception should not occur");
			e.printStackTrace();
			return false;
		}
	}

	private String errorMsg(Query checkedQuery, String info) {
		String caller = Thread.currentThread().getStackTrace()[2]
				.getMethodName();

		String message = "\nTEST [" + caller + "]";
		message += "\nLEARNED [" + learned.getQueryString() + "]";
		message += "\nSUBJECT [" + checkedQuery.getQueryString() + "]";

		if (latestLog != null) {
			message += "\nVIOLATION [" + latestLog.getViolation() + "]";
		}
		message += "\nINFO: " + info;
		latestLog = null;

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
