package de.alksa.log.impl;

import com.google.inject.AbstractModule;

import de.alksa.log.Logger;

public class LogModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).to(SimpleLogger.class);
	}

}
