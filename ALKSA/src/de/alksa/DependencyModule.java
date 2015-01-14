package de.alksa;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import de.alksa.log.Protocol;
import de.alksa.log.impl.SimpleProtocol;
import de.alksa.persistence.StorageDao;
import de.alksa.persistence.impl.Db4oStorageDao;

public class DependencyModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Protocol.class).to(SimpleProtocol.class);
		bind(StorageDao.class).to(Db4oStorageDao.class);
		bindConstant().annotatedWith(Names.named("db4oPath")).to("/tmp/alksa");
	}

}
