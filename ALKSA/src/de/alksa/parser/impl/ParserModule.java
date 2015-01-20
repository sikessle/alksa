package de.alksa.parser.impl;

import com.google.inject.AbstractModule;

import de.alksa.parser.Parser;

public class ParserModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Parser.class).to(VisitorBasedParser.class);
	}

}
