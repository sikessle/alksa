package de.alksa.classifier.impl;

import de.alksa.classifier.Classifier;
import de.alksa.querystorage.Query;

class SimpleClassifier implements Classifier {

	private boolean learning;

	public SimpleClassifier() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean accept(Query query) {
		if (query == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isLearning() {
		return learning;
	}

	@Override
	public void setLearning(boolean learning) {
		this.learning = learning;
	}

}
