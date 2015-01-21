package de.alksa;

import java.util.Objects;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.alksa.classifier.Classifier;
import de.alksa.classifier.impl.ClassifierModule;
import de.alksa.log.impl.LogModule;
import de.alksa.parser.impl.ParserModule;
import de.alksa.persistence.impl.PersistenceModule;
import de.alksa.querystorage.impl.QueryStorageModule;

public class ALKSA {

	private Classifier classifier;

	public ALKSA(String storagePath) {
		Objects.requireNonNull(storagePath);

		Injector injector = Guice.createInjector(new ClassifierModule(),
				new ParserModule(), new LogModule(), new PersistenceModule(
						storagePath), new QueryStorageModule());
		classifier = injector.getInstance(Classifier.class);
	}

	/**
	 * Checks or learns a query.
	 *
	 * @return True if the query was successfully checked. False if an error
	 *         occurred.
	 */
	public boolean accept(String sqlQuery, String database, String databaseUser) {
		return classifier.accept(sqlQuery, database, databaseUser);
	}

	public void setLearning(boolean learning) {
		classifier.setLearning(learning);
	}

	public boolean isLearning() {
		return classifier.isLearning();
	}

}
