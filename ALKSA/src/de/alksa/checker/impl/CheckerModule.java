package de.alksa.checker.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import de.alksa.checker.QueryChecker;

public class CheckerModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<QueryChecker> binder = Multibinder.newSetBinder(binder(),
				QueryChecker.class);
		binder.addBinding().to(SelectColumnListNameChecker.class);
		// TODO checker: remove function tokens, calculation tokens. Check
		// separately
		// TODO checker: fromList..
	}

}
