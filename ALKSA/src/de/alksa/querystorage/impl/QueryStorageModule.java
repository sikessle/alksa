package de.alksa.querystorage.impl;

import com.google.inject.AbstractModule;

import de.alksa.querystorage.Query;
import de.alksa.querystorage.QueryStorage;

public class QueryStorageModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(QueryStorage.class).to(SimpleQueryStorage.class);
		bind(Query.class).to(QueryImpl.class);
	}

}
