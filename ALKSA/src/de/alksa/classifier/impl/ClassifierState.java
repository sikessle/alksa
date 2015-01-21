package de.alksa.classifier.impl;

import de.alksa.querystorage.Query;

interface ClassifierState {

	boolean accept(Query query);

}
