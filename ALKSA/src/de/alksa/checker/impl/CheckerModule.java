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
		binder.addBinding().to(SelectColumnListFunctionChecker.class);
		binder.addBinding().to(SelectColumnListCalculationChecker.class);
		binder.addBinding().to(SelectColumnListSubqueryChecker.class);
		binder.addBinding().to(FromListTableChecker.class);
		// TODO checker: join..
	}

}
