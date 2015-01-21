package de.alksa.classifier.impl;

import java.util.Set;

import de.alksa.querystorage.Query;

public class QueryTestSet {

	private Query learned;
	private Set<Query> allowed;
	private Set<Query> disallowed;

	public QueryTestSet(Query learned, Set<Query> allowed, Set<Query> disallowed) {
		this.learned = learned;
		this.allowed = allowed;
		this.disallowed = disallowed;
	}

	public Query getLearned() {
		return learned;
	}

	public Set<Query> getAllowed() {
		return allowed;
	}

	public Set<Query> getDisallowed() {
		return disallowed;
	}

}
