package de.alksa;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import de.alksa.log.Protocol;
import de.alksa.log.impl.SimpleProtocol;
import de.alksa.persistence.StorageDao;
import de.alksa.persistence.impl.Db4oStorageDao;
import de.alksa.querystorage.QueryStorage;
import de.alksa.querystorage.SimpleQueryStorage;

public class DependencyModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Protocol.class).to(SimpleProtocol.class);
		bind(QueryStorage.class).to(SimpleQueryStorage.class);
		bind(StorageDao.class).to(Db4oStorageDao.class);
		bindConstant().annotatedWith(Names.named("db4oPath")).to("/tmp/alksa");
	}

}