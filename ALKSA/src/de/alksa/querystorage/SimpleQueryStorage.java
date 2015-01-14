package de.alksa.querystorage;

import com.google.inject.Inject;

import de.alksa.persistence.StorageDao;

public class SimpleQueryStorage implements QueryStorage {

	private StorageDao storage;
	
	@Inject
	public SimpleQueryStorage(StorageDao storage) {
		this.storage = storage;
	}
	
	@Override
	public void write(Query query) {
		
	}

}
