package controllers;

import play.Application;
import play.GlobalSettings;
import de.alksa.ALKSA;

public class Global extends GlobalSettings {

	@Override
	public void onStop(Application app) {
		ALKSA alksa = ALKSASingleton.getInstance();
		alksa.shutdown();
	}

}
