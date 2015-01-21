package de.alksa.classifier.impl;

import com.google.inject.AbstractModule;

import de.alksa.classifier.Classifier;

public class ClassifierModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Classifier.class).to(CheckerBasedClassifier.class);
	}

}
