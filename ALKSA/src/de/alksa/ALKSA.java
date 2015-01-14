package de.alksa;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ALKSA {

	private boolean learn;

	public ALKSA() {
		Injector injector = Guice.createInjector(new DependencyModule());
		// TODO build togehter all ALKSA modules
	}

	public void setLearnMode(boolean learn) {
		this.learn = learn;
	}

	public void processQuery(String query, String database, String databaseUser) {
		if (learn) {
			learnQuery(query, database, databaseUser);
		} else {
			checkQuery(query, database, databaseUser);
		}
	}

	private void learnQuery(String query, String database, String databaseUser) {

	}

	private void checkQuery(String query, String database, String databaseUser) {

	}

}
