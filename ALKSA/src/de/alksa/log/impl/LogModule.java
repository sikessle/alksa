package de.alksa.log.impl;

import com.google.inject.AbstractModule;

import de.alksa.log.Protocol;

public class LogModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Protocol.class).to(SimpleProtocol.class);
	}

}
