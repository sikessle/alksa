package de.alksa;

public class ALKSA {

	// private boolean learn;

	public ALKSA() {
		// Injector injector = Guice.createInjector(new ClassifierModule(), new
		// ParserModule(), new LogModule(), new PersistenceModule(), new
		// QueryStorageModule());
		// TODO build together all ALKSA modules
	}

	public void setLearnMode(boolean learn) {
		// delegate to classifier module
	}

	public void processQuery(String query, String database, String databaseUser) {
		// delegate to classifier module
	}

	public void clear() {
		// clear storages
		// clearn learn mode etc.
		// delegate to classifier module
	}

}
