package de.alksa.persistence.impl;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import de.alksa.persistence.StorageDao;

public class PersistenceModule extends AbstractModule {

	private String persistencePath;

	public PersistenceModule(String persistencePath) {
		this.persistencePath = persistencePath;
	}

	@Override
	protected void configure() {
		bind(StorageDao.class).to(Db4oStorageDao.class);
		bindConstant().annotatedWith(Names.named("db4oPath")).to(
				persistencePath);
	}

}
