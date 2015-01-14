package de.alksa;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ALKSA {
	
	public ALKSA() {
		Injector injector = Guice.createInjector(new DependencyModule());
	}
	
}
