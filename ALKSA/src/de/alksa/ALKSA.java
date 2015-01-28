package de.alksa;

import java.util.Objects;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.alksa.checker.impl.CheckerModule;
import de.alksa.classifier.Classifier;
import de.alksa.classifier.impl.ClassifierModule;
import de.alksa.log.LogEntry;
import de.alksa.log.impl.LogModule;
import de.alksa.parser.impl.ParserModule;
import de.alksa.persistence.StorageDao;
import de.alksa.persistence.impl.PersistenceModule;
import de.alksa.querystorage.impl.QueryStorageModule;

public final class ALKSA {

	private Classifier classifier;
	private StorageDao storage;
	private boolean shutdown;

	public ALKSA(String storagePath) {
		Objects.requireNonNull(storagePath);

		Injector injector = Guice.createInjector(new CheckerModule(),
				new ClassifierModule(), new ParserModule(), new LogModule(),
				new PersistenceModule(storagePath), new QueryStorageModule());
		classifier = injector.getInstance(Classifier.class);
		storage = injector.getInstance(StorageDao.class);
	}

	/**
	 * Checks or learns a query.
	 *
	 * @return True if the query was accepted/learned. False if not.
	 * @throws ALKSAInvalidQueryException
	 *             If the query was not a valid SQL statement or if the parser
	 *             could not handle the query.
	 * @throws IllegalStateException
	 *             If shutdown() was called before.
	 */
	public boolean accept(String sqlQuery, String database, String databaseUser)
			throws ALKSAInvalidQueryException, IllegalStateException {
		checkForShutdown();
		return classifier.accept(sqlQuery, database, databaseUser);
	}

	/**
	 * @throws IllegalStateException
	 *             If shutdown() was called before.
	 */
	public void setLearning(boolean learning) throws IllegalStateException {
		checkForShutdown();
		classifier.setLearning(learning);
	}

	/**
	 * @throws IllegalStateException
	 *             If shutdown() was called before.
	 */
	public boolean isLearning() throws IllegalStateException {
		checkForShutdown();
		return classifier.isLearning();
	}

	/**
	 * @throws IllegalStateException
	 *             If shutdown() was called before.
	 */
	public Set<LogEntry> getLogEntries() throws IllegalStateException {
		checkForShutdown();
		return classifier.getLogEntries();
	}

	/**
	 * Shuts down ALKSA and disables all further processing.
	 */
	public void shutdown() {
		shutdown = true;
		storage.close();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		shutdown();
	}

	private void checkForShutdown() {
		if (shutdown) {
			throw new IllegalStateException(
					"ALKSA already shutdown. Re-instantiate to reuse");
		}
	}

}
