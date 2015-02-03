package controllers;

import java.io.File;

import de.alksa.ALKSA;

public final class ALKSASingleton {

	private final static String PATH = "/tmp/eawa";
	private static ALKSA instance;

	private ALKSASingleton() {
		new File(PATH).delete();
	}

	public static ALKSA getInstance() {
		if (instance == null) {
			instance = new ALKSA(PATH);
		}
		return instance;
	}

}
