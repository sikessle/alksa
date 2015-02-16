package de.alksa.classifier.impl;

import java.util.Set;

import de.alksa.querystorage.Query;

abstract class ClassifierState {

	public final boolean accept(Set<Query> queries) {

		for (Query query : queries) {
			if (!acceptSingleQuery(query)) {
				return false;
			}
		}

		return true;
	}

	protected abstract boolean acceptSingleQuery(Query query);

}
